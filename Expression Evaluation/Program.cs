/** ********************************** ID BLOCK ********************************** **
 * 
 *   Due Date:            December 6, 2016
 *   Software Designer:   Thomas Rao
 *   Course:              420-306 (Fall 2016)
 *   Deliverable:         Assignment #4: Stacks & Expression Evaluation
 *   Description:         Infix is an algebraic notation. Postfix expressions, also
 *    known as polish notation, are easier to implement algorithmically than infix
 *    expressions. This program takes several expressions initialized at compile time.
 *    They are translated then evaluated as postfix expressions.
 ** ****************************************************************************** **/

using System;
using System.Collections.Generic;

namespace V6
{
    class Program
    {
        const int NMAX = 5;       // Maximum size of each name string
        const int LSIZE = 5;      // Actual number of infix strings in the data array
        const int NOPNDS = 10;    // Number of operand symbols in the operand array

        /*************************************************************************
                                      KEY DECLARATIONS            
         *************************************************************************/
        static string[] infix = new string[LSIZE] { "C$A$E",    // Array of infix strings
                             "(A+B)*(C-D)",
                             "A$B*C-D+E/F/(G+H)",
                             "((A+B)*C-(D-E))$(F+G)",
                             "A-B/(C*D$E)"  };

        static string[] postfix = new string[LSIZE] { "CAE$$",  // Array of postfix strings
                             "AB+CD-*",
                             "AB$C*D-EF/GH+/+",
                             "AB+C*DE--FG+$",
                             "ABCDE$*/-"  };

        static char[] opnd = new char[NOPNDS] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J' }; // Operands symbols
        static double[] opndval = new double[NOPNDS] { 3, 1, 2, 5, 2, 4, -1, 3, 7, 187 };           // Operand values
        
        static List<double> opndStk;  // Stack of operands
        static List<char> oprStk;     // Stack of operators
        static char topsym;           // Operand at top of opndStk
        static bool und;              // Underflow check for opndStk

        static void Main()
        {
            opndStk = new List<double>();
            oprStk = new List<char>();
            topsym = ' ';
            und = false;

            /*************************************************************************
                             PRINT OUT THE OPERANDS AND THEIR VALUES            
             *************************************************************************/
            Console.WriteLine("\nOPERAND SYMBOLS USED:\n");   //title
            for (int i = 0; i < NOPNDS; i++)
                Console.Write(opnd[i].ToString().PadLeft(5));

            Console.WriteLine("\n\n\nCORRESPONDING OPERAND VALUES:\n");   //title
            for (int i = 0; i < NOPNDS; i++)
                Console.Write(opndval[i].ToString().PadLeft(5));

            Console.WriteLine("\n\n\n");


            /*************************************************************************
                            CONVERT INFIX TO POSTFIX AND OUTPUT RESULT
             *************************************************************************/
            int ifxPad = 24, pfxPad = 24, ansPad = 8; // Total padding for each column
            string separator = new String('═', ifxPad + pfxPad + ansPad + 4);

            string ifx;    // Infix expression
            string pfx;    // Postfix expression
            double ans;    // Answer to the postfix expression

            Console.WriteLine("INFIX TO POSTFIX CONVERSION\n");  // title
            Console.WriteLine($"╔═{separator}═╗");
            Console.WriteLine("║     Infix Expression         Postfix Conversion      Answer  ║");
            for (int i = 0; i < LSIZE; i++)
            {
                ifx = infix[i];          // Get infix expression
                convert(ifx, out pfx);   // Convert to postfix expression
                ans = evaluate(pfx);     // Evaluate expression

                Console.WriteLine($"╠═{separator}═╣");
                Console.Write($"║ { ifx.PadLeft((ifxPad + ifx.Length) / 2).PadRight(ifxPad)} ");
                Console.Write($" { pfx.PadLeft((pfxPad + pfx.Length) / 2).PadRight(pfxPad)} ");
                Console.Write($" { ans.ToString().PadLeft((ansPad + ans.ToString().Length) / 2).PadRight(ansPad)} ║");
                Console.WriteLine();
            }
            Console.WriteLine($"╚═{separator}═╝");
            Console.ReadLine();
        }
        
        /***************************************************************************************** 
                OUTLINE FUNCTION:   formatting function to print n repetitions of char ch
        ******************************************************************************************/
        static void OutLine(int n, char ch)
        {
            for (int q = 0; q < n; q++)
                Console.Write(ch.ToString());
            Console.WriteLine("\n");
        }

        /*****************************************************************************************
                CONVERT FUNCTION
           Conversion is done by adding operands directly to the postfix expression.
           Operators are added onto a stack if they do not occur before the current operator.
           Key variables:
             s: current symbol (either operand or operator)
             topsym: operator found at top of OPRstk
             und: underflow flag
        ******************************************************************************************/
        static void convert(string ifx, out string pfx)
        {
            pfx = "";
            foreach (char s in ifx) {
                if (isOperand(s))                            // Is s an operand?
                    pfx += s;                                // Add s to postfix expression
                else {
                    OPRpopandtest(ref topsym, ref und);      // Pop oprStk if possible. Otherwise, set underflow to true.

                    while (!und && prcd(topsym, s)) {        // No underflow and topsym (operator on the left) takes
                                                             // precedence over s (operator on the right).
                        pfx += topsym;                       // Topsym occurs first. Therefore, append to postfix expression.
                        OPRpopandtest(ref topsym, ref und);  // Get next operator to the left (stack's top value)
                    }

                    if (!und)                                // Operator was found on stack
                        OPRpush(topsym);                     // Append topsym back in.

                    if (und || s != ')')                     // No operator was found on stack or
                                                             // the current symbol is not a scope closer
                        OPRpush(s);                          // Push current operator to stack
                    else
                        OPRpopandtest(ref topsym, ref und);  // Remove topsym, which must be scope opener, from stack permanently.
                                                             // Scope closer is never pushed onto the stack.
                }
            }

            OPRpopandtest(ref topsym, ref und);              // Dumping every operator leftover on the stack
            while (!und)                                     // onto the postfix expression (i.e.: until underflow).
            {
                pfx += topsym;
                OPRpopandtest(ref topsym, ref und);
            }
        }

        /*****************************************************************************************
                EVALUATE FUNCTION
           Evaluation is done one symbol at the time from left to right. Operands
           are pushed it onto the stack. Operators cause the stack to pop two
           values then push the answer onto the stack.
           Key variables:
             op1: operand on the left
             op2: operand on the right
             val: value of op1 and op2 with an operator applied
        ******************************************************************************************/
        static double evaluate(string pfx)
        {
            double op1 ,op2, val;
            foreach (char s in pfx)
                if (isOperand(s))                // Is s an operand?
                    OPNDpush(opndval[s - 'A']);  // Push it to the operand stack
                else                             // Operator found
                {
                    op2 = OPNDpop();             // First pop is the operand on the right of the local expression.
                    op1 = OPNDpop();             // Second pop is the operand on the left of the local expression.
                    val = calc(op1, op2, s);     // Calculate value of this expression.
                    OPNDpush(val);               // Push value back to the operand stack.
                }

            return OPNDpop(); // One value should remain in the stack.
        }

        /*****************************************************************************************
                PRCD FUNCTION
           This determines which operator executes first knowing that 'left' is
           to the left of 'right'. Scope openers are forced onto the operator
           stack and will terminate any operator when popped including scope closers.
           Scope closers force everything out until the first scope opener is found.
        ******************************************************************************************/
        static bool prcd(char left, char right) {
            bool prcd;
            if (left == '(')
                prcd = false;
            else if (right == '(')
                prcd = false;
            else if (right == ')')
                prcd = true;
            else if (left == '$' && right == '$')
                prcd = false;
            else if (rank(left) >= rank(right))
                prcd = true;
            else
                prcd = false;
            return prcd;
        }

        /*****************************************************************************************
                RANK FUNCTION
           Returns the rank of an operator, determining its precedence against other operators.
        ******************************************************************************************/
        static byte rank(char opr) {
            switch (opr) {
                case '+':  // Addition
                case '-':  // Subtraction
                    return 1;
                case '*':  // Multiplication
                case '/':  // Division
                    return 2;
                case '$':  // Exponent
                    return 3;
                default:
                    return 0;
            }
        }

        /*****************************************************************************************
                OPNDPUSH FUNCTION
        ******************************************************************************************/
        static void OPNDpush(double opndVal)
        {
            opndStk.Insert(opndStk.Count, opndVal);
        }

        /*****************************************************************************************
                OPNDPOP FUNCTION
        ******************************************************************************************/
        static double OPNDpop()
        {
            double value = opndStk[opndStk.Count - 1];
            opndStk.RemoveAt(opndStk.Count - 1);
            return value;
        }

        /*****************************************************************************************
                DUMPOPNDSTACK FUNCTION: Outputs content of opndStk in 1 line.
        ******************************************************************************************/
        static void dumpOPNDstack()
        {
            Console.WriteLine(String.Join(" ", opndStk));
        }

        /*****************************************************************************************
                OPRPUSH FUNCTIONS
        ******************************************************************************************/
        static void OPRpush(char opndVal)
        {
            oprStk.Add(opndVal);
        }

        /*****************************************************************************************
                OPRPOPANDTEST FUNCTION: und is to test against underflow in oprStk.
        ******************************************************************************************/
        static void OPRpopandtest(ref char topSym, ref bool und)
        {
            if (!Empty(oprStk))
            {
                topSym = oprStk[oprStk.Count - 1];
                oprStk.RemoveAt(oprStk.Count - 1);
                und = false;
            }
            else
                und = true;
        }

        /*****************************************************************************************
                DUMPOPRSTACK FUNCTION: Outputs content of oprStk in 1 line.
        ******************************************************************************************/
        static void dumpOPRstack()
        {
            Console.WriteLine(String.Join(" ", oprStk));
        }

        /*****************************************************************************************
                EMPTY FUNCTION: Checks if a list is empty regardless of its type.
        ******************************************************************************************/
        static bool Empty<T>(List<T> stk)
        {
            return stk.Count == 0;
        }

        /*****************************************************************************************
                ISOPERAND FUNCTION
        ******************************************************************************************/
        static bool isOperand(char s)
        {
            return s >= 'A' && s <= 'Z';
        }

        /*****************************************************************************************
                CALC FUNCTION
        ******************************************************************************************/
        static double calc(double left, double right, char opr)
        {
            switch (opr)
            {
                case '+': return left + right;
                case '-': return left - right;
                case '*': return left * right;
                case '/': return left / right;
                case '$': return Math.Pow(left, right);
                default: return 0;
            }
        }
    }
}
