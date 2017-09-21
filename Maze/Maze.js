/**
 * Software Designer: Thomas Rao
 * Description: This program creates perfect mazes and solves every maze with minimum movement.
 *  Fireworks are provided upon reaching the red square.
 * Browser Requirements: Chrome 49 or Firefox 41.0 (EcmaScript 6 required)
 * Source: weblog.jamisbuck.org/2011/1/10/maze-generation-prim-s-algorithm
 *
 * Note: For a given double d, (d | 0) = Math.floor(d) in JavaScript.
**/

var MAZE_WIDTH  = 48;	 // Width of maze (number of squares)
var MAZE_HEIGHT = 27;	 // Height of maze (number of squares)
var MAZE_SQUARE_WIDTH  = 30; // Width of a maze square; defined in init()
var MAZE_SQUARE_HEIGHT = 20; // Height of a maze square; defined in init()
var CANVAS_WIDTH  = MAZE_WIDTH * MAZE_SQUARE_WIDTH;	  // Width of canvas; defined in init()
var CANVAS_HEIGHT = MAZE_HEIGHT * MAZE_SQUARE_HEIGHT; // Height of canvas; defined in init()
var SHOW_STEP_COUNTERS = false; // Flag for displaying minimum step counters
var SHOW_SOLUTION      = true; // Flag for showing the solution
var ALLOW_MOUSE_CLICKS = true; // Flag for mouse clicks


var EXTREMITY_POINT_OPAQUE_LEVEL = 0.5;
var SOLUTION_FILTER_OPAQUE_LEVEL = 0.5;
var PLAYER_FILTER_OPAQUE_LEVEL = 1;
var FIREWORKS_LIMIT = 5;
var FIREWORK_MIN_PARTICLES = 100;
var FIREWORK_MAX_PARTICLES = 200;
var GRAVITY = 30;
var FRAME_RATE = 60;

// KeyEvent object containing all key codes.
// Source: http://stackoverflow.com/questions/1465374/javascript-event-keycode-constants
var KeyEvent = {
	DOM_VK_CANCEL: 3,
	DOM_VK_HELP: 6,
	DOM_VK_BACK_SPACE: 8,
	DOM_VK_TAB: 9,
	DOM_VK_CLEAR: 12,
	DOM_VK_RETURN: 13,
	DOM_VK_ENTER: 14,
	DOM_VK_SHIFT: 16,
	DOM_VK_CONTROL: 17,
	DOM_VK_ALT: 18,
	DOM_VK_PAUSE: 19,
	DOM_VK_CAPS_LOCK: 20,
	DOM_VK_ESCAPE: 27,
	DOM_VK_SPACE: 32,
	DOM_VK_PAGE_UP: 33,
	DOM_VK_PAGE_DOWN: 34,
	DOM_VK_END: 35,
	DOM_VK_HOME: 36,
	DOM_VK_LEFT: 37,
	DOM_VK_UP: 38,
	DOM_VK_RIGHT: 39,
	DOM_VK_DOWN: 40,
	DOM_VK_PRINTSCREEN: 44,
	DOM_VK_INSERT: 45,
	DOM_VK_DELETE: 46,
	DOM_VK_0: 48,
	DOM_VK_1: 49,
	DOM_VK_2: 50,
	DOM_VK_3: 51,
	DOM_VK_4: 52,
	DOM_VK_5: 53,
	DOM_VK_6: 54,
	DOM_VK_7: 55,
	DOM_VK_8: 56,
	DOM_VK_9: 57,
	DOM_VK_SEMICOLON: 59,
	DOM_VK_EQUALS: 61,
	DOM_VK_A: 65,
	DOM_VK_B: 66,
	DOM_VK_C: 67,
	DOM_VK_D: 68,
	DOM_VK_E: 69,
	DOM_VK_F: 70,
	DOM_VK_G: 71,
	DOM_VK_H: 72,
	DOM_VK_I: 73,
	DOM_VK_J: 74,
	DOM_VK_K: 75,
	DOM_VK_L: 76,
	DOM_VK_M: 77,
	DOM_VK_N: 78,
	DOM_VK_O: 79,
	DOM_VK_P: 80,
	DOM_VK_Q: 81,
	DOM_VK_R: 82,
	DOM_VK_S: 83,
	DOM_VK_T: 84,
	DOM_VK_U: 85,
	DOM_VK_V: 86,
	DOM_VK_W: 87,
	DOM_VK_X: 88,
	DOM_VK_Y: 89,
	DOM_VK_Z: 90,
	DOM_VK_CONTEXT_MENU: 93,
	DOM_VK_NUMPAD0: 96,
	DOM_VK_NUMPAD1: 97,
	DOM_VK_NUMPAD2: 98,
	DOM_VK_NUMPAD3: 99,
	DOM_VK_NUMPAD4: 100,
	DOM_VK_NUMPAD5: 101,
	DOM_VK_NUMPAD6: 102,
	DOM_VK_NUMPAD7: 103,
	DOM_VK_NUMPAD8: 104,
	DOM_VK_NUMPAD9: 105,
	DOM_VK_MULTIPLY: 106,
	DOM_VK_ADD: 107,
	DOM_VK_SEPARATOR: 108,
	DOM_VK_SUBTRACT: 109,
	DOM_VK_DECIMAL: 110,
	DOM_VK_DIVIDE: 111,
	DOM_VK_F1: 112,
	DOM_VK_F2: 113,
	DOM_VK_F3: 114,
	DOM_VK_F4: 115,
	DOM_VK_F5: 116,
	DOM_VK_F6: 117,
	DOM_VK_F7: 118,
	DOM_VK_F8: 119,
	DOM_VK_F9: 120,
	DOM_VK_F10: 121,
	DOM_VK_F11: 122,
	DOM_VK_F12: 123,
	DOM_VK_F13: 124,
	DOM_VK_F14: 125,
	DOM_VK_F15: 126,
	DOM_VK_F16: 127,
	DOM_VK_F17: 128,
	DOM_VK_F18: 129,
	DOM_VK_F19: 130,
	DOM_VK_F20: 131,
	DOM_VK_F21: 132,
	DOM_VK_F22: 133,
	DOM_VK_F23: 134,
	DOM_VK_F24: 135,
	DOM_VK_NUM_LOCK: 144,
	DOM_VK_SCROLL_LOCK: 145,
	DOM_VK_COMMA: 188,
	DOM_VK_PERIOD: 190,
	DOM_VK_SLASH: 191,
	DOM_VK_BACK_QUOTE: 192,
	DOM_VK_OPEN_BRACKET: 219,
	DOM_VK_BACK_SLASH: 220,
	DOM_VK_CLOSE_BRACKET: 221,
	DOM_VK_QUOTE: 222,
	DOM_VK_META: 224
};


var canvas,   // Canvas as an object
	context,  // Context of canvas
	maze,	 // Maze as an object
	player,   // Player as an object
	fireworks;

var fireworkCancelToken = -1;

var State = { Maze : 0, End : 1 };
var state = State.Maze;

var lfd = new Date().getTime();

var Direction = { North : 1, East : 2, South : 4, West : 8 };

var Square = {
	Empty : 0,
	Full : 15,
	
	Frontier : 16,
	In : 32
};

var FireworkType = {
	FAN : 0,
	FOUR_LAYER_CIRCLE : 1,
	FAN_TYPE_2 : 2
};


function Player(column, row, color) {
	this.col = column;
	this.row = row;
	this.color = color;
	
	this.path = "";
}

Player.prototype.isAt = function(column, row) {
	return this.col == column && this.row == row;
};

Player.prototype.move = function(direction) {
	switch (direction) {
		case Direction.North: this.row--; this.path += "N"; break;
		case Direction.East: this.col++; this.path += "E"; break;
		case Direction.South: this.row++; this.path += "S"; break;
		case Direction.West: this.col--; this.path += "W"; break;
		default: console.error("Unknown player movement:", direction);
	}
};

Player.prototype.render = function() {
	var MAZE_SQUARE_WIDTH = CANVAS_WIDTH / maze.width;
	var MAZE_SQUARE_HEIGHT = CANVAS_HEIGHT / maze.height;
	
	context.globalAlpha = PLAYER_FILTER_OPAQUE_LEVEL;
	context.fillStyle = this.color;
	context.fillRect(this.col * MAZE_SQUARE_WIDTH, this.row * MAZE_SQUARE_HEIGHT, MAZE_SQUARE_WIDTH, MAZE_SQUARE_HEIGHT);
};

Player.prototype.reset = function(column, row) {
	this.col = column;
	this.row = row;
	
	this.path = "";
};


function Firework(type, particleCount, color = (Math.random() * 16777216 | 0).toString(0x10)) {
	this.type = type;
	this.particleCount = particleCount;
	this.color = color;
	
	this.x = -1;
	this.y = -1;
	this.destX = -1;
	this.destY = -1;
	this.destTimer = 0;
	
	this.shot = false;
	this.exploded = false;
	this.particles = [];
};

Firework.prototype.addParticle = function(angle, timer) {
	var r = Math.random() * 0.10 + 0.95;
	switch (this.type) {
		case FireworkType.FAN:
			var f = Math.log(this.particles.length % 9) / Math.log(3) + 1;
			this.particles.push([this.destX, this.destY, 15 * f * Math.cos(angle) * r, 15 * f * Math.sin(angle) * r, timer]);
			break;
		case FireworkType.FOUR_LAYER_CIRCLE:
			var f = (this.particles.length % 4) + 2;
			this.particles.push([this.destX, this.destY, 15 * f * Math.cos(angle) * r, 15 * f * Math.sin(angle) * r, timer]);
			break;
		case FireworkType.FAN_TYPE_2:
			var f = ((this.particles.length / 2) % 5) + 2;
			this.particles.push([this.destX, this.destY, 15 * f * Math.cos(angle) * r, 15 * f * Math.sin(angle) * r, timer]);
			break;
	}
	
};

Firework.prototype.explode = function() {
	this.shot = false;
	this.exploded = true;
}

Firework.prototype.render = function() {
	context.fillStyle = "#" + this.color;
	if (this.shot) {
		// Render firework
		context.globalAlpha = 1;
		context.beginPath();
		context.arc(this.x, this.y, 5, 0, 2 * Math.PI);
		context.closePath();
		context.fill();
	} else {
		// Render particles
		context.globalAlpha = 0.7;
		for (var p in this.particles) {
			context.beginPath();
			context.arc(this.particles[p][0], this.particles[p][1], 5, 0, 2 * Math.PI);
			context.closePath();
			context.fill();
		}
	}
};

Firework.prototype.shoot = function(fromX, fromY, toX, toY, timer = 1500) {
	this.x = fromX;
	this.y = fromY;
	this.destX = toX;
	this.destY = toY;
	this.destTimer = timer;
	
	this.shot = true;
	this.exploded = false;
	
	var explosionTime = new Date().getTime() + timer;
	
	while (this.particles.length < this.particleCount) {
		this.addParticle(2 * Math.PI * this.particles.length / this.particleCount, explosionTime + Math.random() * 5000 + 2500);
	}
};

Firework.prototype.update = function(ms) {
	// Get time difference since last frame update.
	var dms = ms - lfd;
	var ds = dms / 1000;
	
	// Use time based movement (from ship.js).
	if (this.exploded) {
		// Update particles
		for (var p = 0; p < this.particles.length; p++) {
			if (this.particles[p][4] > ms) {
				var vx = this.particles[p][2];
				var vy = this.particles[p][3];
				var dx = vx * ds;
				var dy = vy * ds + GRAVITY * ds * ds / 2;
				
				this.particles[p][0] += dx;
				this.particles[p][1] += dy;
				this.particles[p][3] += GRAVITY * ds;
				this.particles[p][4] -= dms;
			} else
				this.particles.splice(p--, 1);
		}
	} else if (this.shot) {
		// Update firework
		// Calcuate distance and angle of movement.
		var dx = this.destX - this.x;
		var dy = this.destY - this.y;
		var distance = Math.sqrt(dx * dx + dy * dy);
		var alpha = (dx < 0 ? Math.PI : 0) + Math.atan(-dy/dx);
		
		// Check if it should have arrived already
		if (this.destTimer - dms <= 0) {
			// Make sure it has arrived at destination.
			this.x = Math.min(Math.max(this.destX, 5 / 2), CANVAS_WIDTH - 5 / 2);
			this.y = Math.min(Math.max(this.destY, 5 / 2), CANVAS_HEIGHT - 5 / 2);
			// Stop movement.
			this.explode();
		} else {
			// Dynamic linear movement
			var pms = dms / this.destTimer;
			this.x += pms * distance * Math.cos(alpha);
			this.y -= pms * distance * Math.sin(alpha);
			
			// Constrain this thing to canvas.
			this.x = Math.max(Math.min(this.x, CANVAS_WIDTH - 5 / 2), 5 / 2);
			this.y = Math.max(Math.min(this.y, CANVAS_HEIGHT - 5 / 2), 5 / 2);
			
			// Decrease timer by time since last update.
			this.destTimer -= dms;
		}
	}
};


function Maze(width, height, startX = Math.random() * MAZE_WIDTH | 0, startY = Math.random() * MAZE_HEIGHT | 0) {
	this.width = width;
	this.height = height;
	
	this.startX = startX;
	this.startY = startY;
	this.endX = -1;
	this.endY = -1;
	
	this.grid = null;
	this.sequences = null;
	this.solution = null;
	this.solutionsShown = SHOW_SOLUTION;
	
	this.create();
	this.createDistanceSheet();
	
	var endings = this.findLongestSequences();
	var end = Math.random() * endings.length | 0;
	this.setEndpoint(endings[end][0], endings[end][1]);
};

Maze.prototype.create = function() {
	var ms = new Date().getTime();
	var m = this;
	
	function mark(col, row, frontier, counter) {
		m.grid[row][col] |= Square.In;
		
		var neighbors = m.getNeighbors(col, row);
		var x, y;
		for (var n in neighbors) {
			x = neighbors[n][0];
			y = neighbors[n][1];
			if (m.isPositionValid(x, y) && m.get(x, y) == Square.Full) {
				m.grid[y][x] |= Square.Frontier;
				frontier.push([x, y, counter]);
			}
		}
	}
	
	// Create the grid
	this.grid = [];
	for (var row = 0; row < this.height; row++) {
		this.grid.push([]);
		for (var col = 0; col < this.width; col++) {
			this.grid[row].push(Square.Full);
		}
	}
	
	var frontiers = [];
	mark(this.startX, this.startY, frontiers, 0);
	
	var i, x, y, d, c;
	var n, ns, nx, ny, nd;
	while (frontiers.length > 0) {
		i = (1 - 0.5 * Math.random() * Math.random()) * frontiers.length | 0; // Get index of current position
		x = frontiers[i][0];	// Get current horizontal position
		y = frontiers[i][1];	// Get current vertical position
		c = frontiers[i][2];	// Get current counter
		frontiers.splice(i, 1); // Remove this data set
		
		n = this.getNeighbors(x, y, true); // Get neighbors inside the maze
		ns = Math.random() * n.length | 0; // Select random neighbor
		
		nx = n[ns][0]; // Get neighbor's horizontal position
		ny = n[ns][1]; // Get neighbor's vertical position
		nd = n[ns][2]; // Get direction from neighbor to current position
		d = this.getOppositeDirection(nd); // Get direction from current position to neighbor
		
		this.grid[y][x] ^= d;	 // Remove wall between current position and neighbor
		this.grid[ny][nx] ^= nd; // Remove wall between neighbor and current position
		
		mark(x, y, frontiers, c + 1); // Include this position as part of the maze
		frontiers.sort(function(a, b) { return a[3] - b[3]; });
	}
	
	console.log("Completed grid in", new Date().getTime() - ms, "ms.");
};

Maze.prototype.createDistanceSheet = function(fromX = this.startX, fromY = this.startY) {
	var ms = new Date().getTime(); // Get current time in milliseconds
	
	function getInstruction(direction) {
		switch (direction) {
			case Direction.North: return "N";
			case Direction.East: return "E";
			case Direction.South: return "S";
			case Direction.West: return "W";
			case "": return "";
			default: return " ";
		}
	}
	
	function getDirection(instruction) {
		switch (instruction) {
			case "N": return Direction.North;
			case "E": return Direction.East;
			case "S": return Direction.South;
			case "W": return Direction.West;
			default: return null;
		}
	}
	
	this.sequences = []; // Sequence sheet
	var g = [];		     // Walls reference sheet
	// Creating sequence and wall reference sheets
	for (var r = 0; r < this.height; r++) {
		this.sequences.push([]);
		g.push([]);
		for (var c = 0; c < this.width; c++) {
			this.sequences[r].push(null);
			g[r].push(this.grid[r][c]);
		}
	}
	
	var x;         // Current horizontal position
	var y;		   // Current vertical position
	var neighbors; // List of neighbors at current position
	var col;       // Horizontal position of current neighbor.
	var row;       // Vertical position of current neighbor
	var cd;        // Direction from current position to neighbor
	var od;        // Opposite direction of current one
	var path;      // Path from starting point
	var pi;	       // Last instruction for path
	var frontiers = []; // List of neighbor of current cells inside maze
	
	frontiers.push([fromX, fromY, "", ""]); // Giving a starting point.
	this.sequences[fromY][fromX] = "";	  // Starting point requires no movement
	
	while (frontiers.length > 0) {
		x = frontiers[0][0];                    // Get current horizontal position
		y = frontiers[0][1];                    // Get current vertical position
		path = frontiers[0][2];                 // Get path created so far
		pi = getInstruction(frontiers[0][3]);   // Get last instruction given
		frontiers.shift();                      // Remove set of data
		
		neighbors = this.getNeighbors(x, y);    // Get list of neighbors from current position
		for (var n in neighbors) {
			col = neighbors[n][0];              // Get neighbor's horizontal position
			row = neighbors[n][1];              // Get neighbor's vertical position
			od = neighbors[n][2];               // Get direction from neighbor to current position
			cd = this.getOppositeDirection(od); // Get direction from current position to neighbor
			
			if ((g[y][x] & cd) != cd) { // Is no wall present?
				if (this.sequences[row][col] == null) { // Is this path new?
					this.sequences[row][col] = path + pi + getInstruction(cd); // Update path
					frontiers.push([col, row, path + pi, cd]);                 // Add new candidate to search.
				}
				g[y][x] |= cd;     // Close wall
				g[row][col] |= od; // Close wall on other side as well
			}
		}
	}
	
	console.log("Completed sequence sheet in", new Date().getTime() - ms, "ms.");
};

Maze.prototype.findLongestSequences = function() {
	if (this.sequences == null) {
		alert("Lack of sequence sheet.");
		return [];
	}
	
	var sequence = "", longest = 0;
	var positions = [];
	
	for (var row = 0; row < this.height; row++) { // Iterate through every slot
		for (var col = 0; col < this.width; col++) {
			sequence = this.sequences[row][col];
			if (sequence == null) {
				continue;
			} else if (sequence.length > longest) {  // New longest sequence found
				longest = sequence.length;           // Delete previous list then add this to list.
				positions.splice(0, positions.length, [col, row]);
			} else if (sequence.length == longest) { // Equally long sequence found
				positions.push([col, row]);          // Add to current list
			}
		}
	}
	
	return positions;
};

Maze.prototype.get = function(x, y, direction = null) {
	if (direction != null) {
		var position = this.getPosition(x, y, direction);
		if (position != null)
			return this.grid[position[1]][position[0]];
		return null;
	}
	return this.grid[y][x];
};

Maze.prototype.getNeighbors = function(x, y, inside = false) {
	var neighbors = [];
	var position;
	for (var dir in Direction) {
		position = this.getPosition(x, y, Direction[dir]);
		
		if (position != null && (!inside || this.isInsideAt(position[0], position[1]))) {
			neighbors.push(position);
		}
	}
	return neighbors;
};

Maze.prototype.getOppositeDirection = function(direction) {
	switch (direction) {
		case Direction.North: return Direction.South;
		case Direction.East:  return Direction.West;
		case Direction.South: return Direction.North;
		case Direction.West:  return Direction.East;
		default: return null;
	}
};

Maze.prototype.getPosition = function(x, y, direction) {
	switch (direction) {
		case Direction.North:
			if (y - 1 >= 0)
				return [x, y - 1, Direction.South];
			break;
		case Direction.East:
			if (x + 1 < this.grid[y].length)
				return [x + 1, y, Direction.West];
			break;
		case Direction.South:
			if (y + 1 < this.grid.length)
				return [x, y + 1, Direction.North];
			break;
		case Direction.West:
			if (x - 1 >= 0)
				return [x - 1, y, Direction.East];
			break;
		default:
			console.log("Invalid argument 'direction':", direction);
			break;
	}
	return null;
}

// TODO: get solution from specific point.
Maze.prototype.getSolution = function(fromX = this.startX, fromY = this.startY, toX = this.endX, toY = this.endY) {
	var solution = []; // Initialize the solution nested array
	for (var r = 0; r < this.height; r++) {
		solution.unshift([]);
		for (var c = 0; c < this.width; c++) {
			solution[0].push(false);
		}
	}
	
	var x = this.startX, y = this.startY;
	var path = this.sequences != null && this.sequences[this.endY] != undefined && this.sequences[this.endY][this.endX] != null ? this.sequences[this.endY][this.endX] : "";
	
	while (path.length > 1) { // Exclude ending square
		switch (path[0]) { // Move
			case 'N': y--; break;
			case 'E': x++; break;
			case 'S': y++; break;
			case 'W': x--; break;
			default: console.log("Unknown instruction:", path[0]); break;
		}
		
		solution[y][x] = true;
		path = path.substring(1); // Get next instruction.
	}
	return solution;
}

Maze.prototype.hasWall = function(x, y, wall) {
	return (this.get(x, y) & wall) == wall;
}

Maze.prototype.isEmptyAt = function(x, y) {
	return this.get(x, y) == Square.Empty;
}

Maze.prototype.isFullAt = function(x, y) {
	return this.get(x, y) == Square.Full;	
}

Maze.prototype.isInsideAt = function(x, y) {
	return (this.get(x, y) & Square.In) == Square.In;	
}

Maze.prototype.isPositionValid = function(x, y) {
	return y >= 0 && y < this.grid.length && x >= 0 && x < this.grid[y].length;
}

Maze.prototype.movePlayer = function(player, direction) {
	if (!this.hasWall(player.col, player.row, direction)) {
		player.move(direction);
		if (player.isAt(maze.endX, maze.endY)) {
			state = State.End;
			fireworks = [];
			while (fireworks.length < FIREWORKS_LIMIT) {
				fireworks.unshift(new Firework(Math.random() * 3 | 0, Math.random() * (FIREWORK_MAX_PARTICLES - FIREWORK_MIN_PARTICLES) + FIREWORK_MIN_PARTICLES));
				fireworks[0].shoot(CANVAS_WIDTH * (Math.random() * 0.75 + 0.125), CANVAS_HEIGHT * (Math.random() * 0.25 + 1), CANVAS_WIDTH * (Math.random() * 0.75 + 0.125), CANVAS_HEIGHT * (Math.random() * 0.25 + 0.15), (Math.random() * 1000 | 0) + 500);
			}
			lfd = new Date().getTime();
			fireworkCancelToken = setInterval(drawFireworks, 1000 / FRAME_RATE);
		}
	}
}

Maze.prototype.removeEndpoint = function() {
	this.endX = -1;
	this.endY = -1;
	this.solution = null;
}

Maze.prototype.render = function() {
	var ms = new Date().getTime();
	
	context.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
	if (typeof this.grid == undefined || this.grid.length != this.height || this.height > 0 && this.grid[0].length != this.width) {
		console.log("Maze failed to render.");
		return;
	}
	
	for (var row = 0; row < this.height; row++) {
		for (var col = 0; col < this.width; col++) {
			this.renderSquare(col, row);
		}
	}
	
	console.log("Maze render took", new Date().getTime() - ms, "ms.");
}

Maze.prototype.renderSquare = function(col, row, filter = true) {
	context.clearRect(col * MAZE_SQUARE_WIDTH, row * MAZE_SQUARE_HEIGHT, MAZE_SQUARE_WIDTH, MAZE_SQUARE_HEIGHT);
	
	context.fillStyle = "#f00";
	context.strokeStyle = "#000";
	context.textBaseline = "middle";
	context.textAlign = "center";
	
	// Applying a filter if necessary on the square (i.e.: starting point).
	if (filter) {
		if (col == this.endX && row == this.endY) {
			this.renderEndingSquareFilter(col, row);
		} else if (col == this.startX && row == this.startY) {
			this.renderStartingSquareFilter(col, row);
		} else if (this.solutionsShown && this.solution != null && this.solution[row][col]) {
			this.renderSolutionSquareFilter(col, row);
		}
	}
	
	context.globalAlpha = 1;
	context.beginPath();
	for (var d in Direction) {
		if (this.hasWall(col, row, Direction[d])) {
			switch (Direction[d]) {
				case Direction.North:
					context.moveTo(col * MAZE_SQUARE_WIDTH, row * MAZE_SQUARE_HEIGHT);
					context.lineTo((col + 1) * MAZE_SQUARE_WIDTH, row * MAZE_SQUARE_HEIGHT);
				break;
				case Direction.East:
					context.moveTo((col + 1) * MAZE_SQUARE_WIDTH, row * MAZE_SQUARE_HEIGHT);
					context.lineTo((col + 1) * MAZE_SQUARE_WIDTH, (row + 1) * MAZE_SQUARE_HEIGHT);
				break
				case Direction.South:
					context.moveTo(col * MAZE_SQUARE_WIDTH, (row + 1) * MAZE_SQUARE_HEIGHT);
					context.lineTo((col + 1) * MAZE_SQUARE_WIDTH, (row + 1) * MAZE_SQUARE_HEIGHT);
				break;
				case Direction.West:
					context.moveTo(col * MAZE_SQUARE_WIDTH, row * MAZE_SQUARE_HEIGHT);
					context.lineTo(col * MAZE_SQUARE_WIDTH, (row + 1) * MAZE_SQUARE_HEIGHT);
				break;
				default:
					console.log("Unknown wall rendered:", Direction[d]);
				break;
			}
		}
		
	}
	context.closePath();
	context.stroke();
	
	if (SHOW_STEP_COUNTERS)
	context.fillText(this.sequences[row][col].length, (col + 0.5) * MAZE_SQUARE_WIDTH, (row + 0.5) * MAZE_SQUARE_HEIGHT);
}

Maze.prototype.renderWholeSolution = function() {
	if (this.sequences == null || !this.isPositionValid(this.endX, this.endY))
		return;
	
	var x = this.startX, y = this.startY;
	var path = this.sequences != null && this.sequences[this.endY] != undefined && this.sequences[this.endY][this.endX] != null ? this.sequences[this.endY][this.endX] : "";
	
	context.globalAlpha = SOLUTION_FILTER_OPAQUE_LEVEL;
	while (path.length > 0) {
		switch (path[0]) { // Move
			case 'N': y--; break;
			case 'E': x++; break;
			case 'S': y++; break;
			case 'W': x--; break;
			default: console.log("Unknown instruction:", path[0]); break;
		}
		this.renderSquare(x, y);
		path = path.substring(1); // Get next instruction.
	}
}

Maze.prototype.renderEndingSquareFilter = function(column = this.endX, row = this.endY) {
	context.globalAlpha = EXTREMITY_POINT_OPAQUE_LEVEL;
	context.fillStyle = "#F00";
	context.fillRect(column * MAZE_SQUARE_WIDTH, row * MAZE_SQUARE_HEIGHT, MAZE_SQUARE_WIDTH, MAZE_SQUARE_HEIGHT);
}

Maze.prototype.renderSolutionSquareFilter = function(column, row) {
	context.globalAlpha = SOLUTION_FILTER_OPAQUE_LEVEL;
	context.fillStyle = "#87CEFA";
	context.fillRect(column * MAZE_SQUARE_WIDTH, row * MAZE_SQUARE_HEIGHT, MAZE_SQUARE_WIDTH, MAZE_SQUARE_HEIGHT);
}

Maze.prototype.renderStartingSquareFilter = function(column = this.startX, row = this.startY) {
	context.globalAlpha = EXTREMITY_POINT_OPAQUE_LEVEL;
	context.fillStyle = "#0F0";
	context.fillRect(column * MAZE_SQUARE_WIDTH, row * MAZE_SQUARE_HEIGHT, MAZE_SQUARE_WIDTH, MAZE_SQUARE_HEIGHT);
}

Maze.prototype.reset = function(sx = Math.random() * MAZE_WIDTH | 0, sy = Math.random() * MAZE_HEIGHT | 0) {
	for (var row = 0; row < this.height; row++) {
		for (var col = 0; col < this.width; col++) {
			this.grid[row][col] = Square.Full;
		}
	}
	
	this.startX = sx;
	this.startY = sy;
	
	this.create();
	this.createDistanceSheet();
	
	this.removeEndpoint();
	var endings = this.findLongestSequences();
	var end = Math.random() * endings.length | 0;
	this.setEndpoint(endings[end][0], endings[end][1]);
	
	this.solutionsShown = SHOW_SOLUTION;
}

Maze.prototype.setEndpoint = function(column, row) {
	if (this.endX != column || this.endY != row) { // Update endpoint & solution only if it changed.
		this.endX = column;
		this.endY = row;
		this.solution = this.getSolution();
	}
}


document.addEventListener("DOMContentLoaded", function() {
	maze = new Maze(MAZE_WIDTH, MAZE_HEIGHT, 0, 0);
	player = new Player(maze.startX, maze.startY, "#FFA500");
	fireworks = [];
	
	canvas = document.getElementById("canvas");
	context = canvas.getContext("2d");
	canvas.width = CANVAS_WIDTH;
	canvas.height = CANVAS_HEIGHT;
	
	
	canvas.addEventListener("click", function(e) {
		onClick(e);
		e.preventDefault();
	} );
	
	document.addEventListener("keydown", function(e) {
		onKeyDown(e);
	} );
	
	document.getElementById("create").addEventListener("click", function(e) {
		if (state == State.End) {
			clearInterval(fireworkCancelToken);
			fireworkCancelToken = -1;
			fireworks = [];
			state = State.Maze;
		}
		maze.reset(player.col, player.row);
		player.reset(maze.startX, maze.startY);
		maze.render();
		player.render();
	} );
	
	document.getElementById("endpoint").addEventListener("click", function(e) {
		var oldCol = maze.endX, oldRow = maze.endY;
		
		var endings = maze.findLongestSequences();
		var end = Math.random() * endings.length | 0;
		maze.setEndpoint(endings[end][0], endings[end][1]);
		
		if (maze.endX != oldCol || maze.endY != oldRow || maze.solutionsShown != SHOW_SOLUTION) {
			maze.solutionsShown = SHOW_SOLUTION;
			maze.render();
			player.render();
		}
	} );
	
	document.getElementById("solution").addEventListener("click", function(e) {
		if (!maze.isPositionValid(maze.endX, maze.endY)) {
			alert("Set an endpoint for the maze.");
			return;
		}
		
		maze.solutionsShown = !maze.solutionsShown;
		maze.renderWholeSolution();
		if (maze.solution != null && maze.solution[player.row][player.col])
			player.render();
	} );
	
	document.getElementById("reset").addEventListener("click", function(e) {
		var oldCol = player.col, oldRow = player.row;
		player.reset(maze.startX, maze.startY);
		
		if (oldCol != player.col || oldRow != player.row) {
			maze.renderSquare(oldCol, oldRow);
			player.render();
		}
	} );
	
	
	maze.render();
	player.render();
} );

function onClick(e) {
	if (!ALLOW_MOUSE_CLICKS || state == State.End)
		return;
	
	var bounds = canvas.getBoundingClientRect();
	maze.setEndpoint((e.clientX - bounds.left) / CANVAS_WIDTH * MAZE_WIDTH | 0, (e.clientY - bounds.top) / CANVAS_HEIGHT * MAZE_HEIGHT | 0);
	maze.render();
	player.render();
}

function onKeyDown(e) {
	if (state != State.Maze)
		return;
	
	var oldCol = player.col, oldRow = player.row;
	switch (e.keyCode) {
		case KeyEvent.DOM_VK_UP:
		case KeyEvent.DOM_VK_W:
		case KeyEvent.DOM_VK_NUMPAD8:
			maze.movePlayer(player, Direction.North);
		break;
		case KeyEvent.DOM_VK_RIGHT:
		case KeyEvent.DOM_VK_D:
		case KeyEvent.DOM_VK_NUMPAD6:
			maze.movePlayer(player, Direction.East);
		break;
		case KeyEvent.DOM_VK_DOWN:
		case KeyEvent.DOM_VK_S:
		case KeyEvent.DOM_VK_NUMPAD2:
			maze.movePlayer(player, Direction.South);
		break;
		case KeyEvent.DOM_VK_LEFT:
		case KeyEvent.DOM_VK_A:
		case KeyEvent.DOM_VK_NUMPAD4:
			maze.movePlayer(player, Direction.West);
		break;
		default:
			console.log("Key down:", e.keyCode);
		return;
	}
	
	if (oldCol != player.col || oldRow != player.row) {
		maze.renderSquare(oldCol, oldRow);
		player.render();
	}
	e.preventDefault();
}

function drawFireworks() {
	var ms = new Date().getTime();
	
	context.fillStyle = "#fff";
	context.globalAlpha = 0.40;
	context.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
	for (var f = 0; f < fireworks.length; f++) {
		if (fireworks[f].particles.length == 0) {
			fireworks[f] = new Firework(Math.random() * 3 | 0, Math.random() * (FIREWORK_MAX_PARTICLES - FIREWORK_MIN_PARTICLES + 1) + FIREWORK_MIN_PARTICLES | 0);
			fireworks[f].shoot(CANVAS_WIDTH * (Math.random() * 0.75 + 0.125), CANVAS_HEIGHT * (Math.random() * 0.25 + 1), CANVAS_WIDTH * (Math.random() * 0.75 + 0.125), CANVAS_HEIGHT * (Math.random() * 0.25 + 0.15), (Math.random() | 0) * 2000 + 500);
		} else 
			fireworks[f].update(ms);
		fireworks[f].render();
	}
	
	lfd = ms;
}