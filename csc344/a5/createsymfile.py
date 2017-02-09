from __future__ import with_statement
import os
import re

path = "/Users/keithmartin/Documents/csc344"

state = 0

def file(path,text_file,ends_with,reg_exp):
    for root,subFolders, files in os.walk(path):
                            for name in files:
                                if name.endswith(ends_with):
                                    new_set = set([])
                                    with open(os.path.join(root, name), 'r') as fin:
                                        writefile(fin,new_set,text_file,name,reg_exp)


def writefile(fin,new_set,text_file,name,reg_exp):
    state = 0
    for lines in fin:
      if state == 1:
         state = 0
      for word in lines.replace('(',' ').replace(';',' ').replace(')',' ').replace('=',' ').replace('{',' ').replace('}',' ').replace(':',' ').split():
        if state == 0 and word.startswith('"') or state == 0 and word.startswith("/*") or state == 0 and word.startswith("#") or state == 0 and word.startswith("//"):
            state = 1
        if state == 0:
                match = re.findall(reg_exp,word)
                for i in match:
                    if i not in new_set:
                        new_set.add(i)
                        text_file.write("[" + name + ", " + i + "]" + "\n")


def createsym():
    for root, subFolders, files in os.walk(path):
        os.chdir(path)
        for sF in subFolders:
            if sF == "a1":
                new_path = os.path.abspath(sF)
                os.chdir(new_path)
                with open('c.txt', 'w') as text_file_c:
                    file(new_path,text_file_c,".c","^[a-zA-Z_][a-zA-Z0-9_;]*$")

    for root, subFolders, files in os.walk(path):
            os.chdir(path)
            for sF in subFolders:
                if sF == "a2":
                    new_path = os.path.abspath(sF)
                    os.chdir(new_path)
                    with open('lisp.txt', 'w') as text_file_lisp:
                            file(new_path,text_file_lisp,".lisp","[-+*/a-zA-Z_][a-zA-Z0-9_-]*")

    for root, subFolders, files in os.walk(path):
            os.chdir(path)
            for sF in subFolders:
                if sF == "a3":
                    new_path = os.path.abspath(sF)
                    os.chdir(new_path)
                    with open('scala.txt', 'w') as text_file_scala:
                        file(new_path,text_file_scala,".scala","[a-zA-Z_][a-zA-Z0-9_]*")

    for root, subFolders, files in os.walk(path):
            os.chdir(path)
            for sF in subFolders:
                if sF == "a4":
                    new_path = os.path.abspath(sF)
                    os.chdir(new_path)
                    with open('pl.txt', 'w') as text_file_prolog:
                            with open('scala.txt', 'w') as text_file_scala:
                                file(new_path,text_file_prolog,".pl","[a-zA-Z_][a-zA-Z0-9_]*")
                                file(new_path,text_file_scala,".scala","[a-zA-Z_][a-zA-Z0-9_]*")


    for root, subFolders, files in os.walk(path):
            os.chdir(path)
            for sF in subFolders:
                if sF == "a5":
                    new_path = os.path.abspath(sF)
                    os.chdir(new_path)
                    with open('py.txt', 'w') as text_file_py:
                        file(new_path,text_file_py,".py","[a-zA-Z_][a-zA-Z0-9_]*")

createsym()