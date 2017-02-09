from __future__ import with_statement
import os

path = "/Users/keithmartin/Documents/csc344"


def create():
    os.chdir("/Users/keithmartin/Documents/csc344")
    html_file = open('csc344.html', 'w')
    html_file.write("<!DOCTYPE html>")
    html_file.write("<html> <head> <title>CSC344 WEB PAGE</title> </head>")
    html_file.write("<body> <p> Assignment 1 </p> </body>")
    html_file.write('<p><a href="a1/main.c">a1/main.c</a></p>'  + " LINES: " + str(sum(1 for line in open("a1/main.c"))))
    html_file.write("<body> <p> Assignment 2 </p> </body>")
    html_file.write('<p><a href="a2/lispassignment.lisp">a2/lispassignment.lisp</a></p>' + " LINES: " + str(sum(1 for line in open("a2/lispassignment.lisp"))) )
    html_file.write("<body> <p> Assignment 3 </p> </body>")
    html_file.write('<p><a href="a3/Main.scala">a3/Main.scala</a></p>' + " LINES: " + str(sum(1 for line in open("a3/Main.scala"))))
    html_file.write("<body> <p> Assignment 4 </p> </body>")
    html_file.write('<p><a href="a4/csc344assignment4.scala">a4/csc344assignment4.scala</a></p>' + " LINES: " + str(sum(1 for line in open("a4/csc344assignment4.scala"))))
    html_file.write('<p><a href="a4/csc344assignment4.pl">a4/csc344assignment4.pl</a></p>' + " LINES: " + str(sum(1 for line in open("a4/csc344assignment4.pl"))))
    html_file.write("<body> <p> Assignment 5 </p> </body>")
    html_file.write('<p><a href="a5/sendmail.py">a5/sendmail.py</a></p>'  + " LINES: " + str(sum(1 for line in open("a5/sendmail.py"))))
    html_file.write('<p><a href="a5/main.py">a5/main.py</a></p>'   + " LINES: " + str(sum(1 for line in open("a5/main.py"))))
    html_file.write('<p><a href="a5/createsymfile.py">a5/createsymfile.py</a></p>' + " LINES: " + str(sum(1 for line in open("a5/createsymfile.py"))))
    html_file.write('<p><a href="a5/createhtml.py">a5/createhtml.py</a></p>' + " LINES: " + str(sum(1 for line in open("a5/createhtml.py"))))
    html_file.write("</html>")

