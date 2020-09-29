#!/usr/bin/env python
import os
import time


class color:
    HEADER = '\033[95m'
    OKBLUE = '\033[94m'
    OKGREEN = '\033[92m'
    WARNING = '\033[93m'
    FAIL = '\033[91m'
    ENDC = '\033[0m'
    BOLD = '\033[1m'
print(color.OKGREEN+"==================================================")
print("                /eeeeeeeeeee\ ");
print("  /RRRRRRRRRR\ /eeeeeeeeeeeee\ /RRRRRRRRRR\ ");
print(" /RRRRRRRRRRRR\|eeeeeeeeeeeee|/RRRRRRRRRRRR\ ");
print("/RRRRRRRRRRRRRR +++++++++++++ RRRRRRRRRRRRRR\ ");
print("|RRRRRRRRRRRRRR ############### RRRRRRRRRRRRRR| ");
print("|RRRRRRRRRRRRR ######### ####### RRRRRRRRRRRRR| ");
print(" \RRRRRRRRRRR ######### ######### RRRRRRRRRR/ ");
print("  |RRRRRRRRR ########## ######## RRRRRRRR| ");
print("  |RRRRRRRRRR ################### RRRRRRRRR| ");
print("               ######     ###### ");
print("               #####       ##### ");
print("               #nnn#       #nnn#");
print("==================================================")
print(color.OKBLUE+color.BOLD+"               HELLO TO OUR GL53"+color.ENDC);
print(color.OKGREEN+"=================================================="+color.ENDC)



while(1):

    print("Que Voulez-Vous Tester ? \n\n" +
    " [1 (Lexer)] \n [2 (Parser)] \n [3 (Context)] \n [4 (gencode)] \n [5 (All)] \n [0 (Quitter)]\n")
    try:
        x = int(input("Veuillez choisir un nombre entre 0 et 5: "))

    except SyntaxError:
        print(color.WARNING+"\nNot an integer! Try again.\n"+color.ENDC)
        continue
    except NameError:
        print(color.WARNING+"\nNot an integer! Try again.\n"+color.ENDC)
    else:
        if (x not in [0, 1, 2, 3, 4, 5]):
            continue
        if x == 0:
            break
        if x == 1:
            os.system("./test_lexer.py")
        elif x == 2:
            os.system("./test_synt.py")
        elif x==3:
            os.system("./test_context.py")
        elif x ==4:
            os.system("./test_gencode.py")
        elif x==5:
            start_time = time.time()

            print("=========================")
            print(color.OKBLUE+"       Test Lexer"+color.ENDC)
            print("=========================")
            os.system("./test_lexer.py")

            print("=========================")
            print(color.OKBLUE+"       Test Synt"+color.ENDC)
            print("=========================")
            os.system("./test_synt.py")

            print("=========================")
            print(color.OKBLUE+"       Test Context"+color.ENDC)
            print("=========================")
            os.system("./test_context.py")

            print("=========================")
            print(color.OKBLUE+"       Test Gencode"+color.ENDC)
            print("=========================")
            os.system("./test_gencode.py")
            print(color.BOLD+color.OKBLUE+"      ---  Execution Time : %.2s secondes ---" % (time.time() - start_time)+color.ENDC)
            print("=========================")
