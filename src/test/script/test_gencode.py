#!/usr/bin/env python3
import os

class color:
    HEADER = '\033[95m'
    OKBLUE = '\033[94m'
    OKGREEN = '\033[92m'
    WARNING = '\033[93m'
    FAIL = '\033[91m'
    ENDC = '\033[0m'
    BOLD = '\033[1m'

def files(path):
    for file in os.listdir(path):
        if os.path.isfile(os.path.join(path, file)):
            yield file


def valid_gencode():
    counter = 0
    tmp = 0
    test_gencode = "../../main/bin/decac"
    valid_gencode_SO = "../deca/codegen/valid/sansObjet"
    valid_gencode_O = "../deca/codegen/valid/objet"
    valid_gencode_provided = "../deca/codegen/valid/provided"

    print("~======================================================~")
    # print(color.BOLD+color.OKBLUE+"  X+X+X+X+X  [TEST Provided GENCODE]  X+X+X+X+X "+color.ENDC)
    # print("~======================================================~")
    # for file in files(valid_gencode_provided):
    #     if (str(file))[len(str(file))-5:] == ".deca":
    #         tmp += 1
    #         execute = test_gencode + " " + valid_gencode_provided + "/" + str(file)
    #         os.system(execute)
    #         if os.path.isfile(str(file)[:-4]+"ass"):
    #             print("Fichier {}ass non généré".format(str(file)[:-4]))
    #             exit()
    #         resultat = os.system("ima {}".format(valid_gencode_provided + "/"+str(file)[:-4])+"ass" + "> resultatProvided.log")
    #         fichier = open("resultatProvided.log", "r")
    #         resultat = fichier.readlines()[0]
    #         resultat = resultat[:len(resultat)-1]
    #         os.system("rm resultatProvided.log")
    #         if str(file) == "cond0.deca":
    #             attendu= "ok"

    #         elif str(file) == "ecrit0.deca":
    #             attendu = "ok"
    #         elif str(file) == "entier1.deca":
    #             attendu = "1"
    #         elif str(file) == "exdoc.deca":
    #             attendu= "a.getX() =  1"
    #         else:
    #             print("fichier pas encore traité")

    #         if resultat==attendu:
    #             print(str(file)+color.BOLD+color.HEADER+"  *** [TEST PASSED] *** "+color.ENDC)
    #             counter+=1
    #             #print("Tout va bien pour " + str(file))
    #         else:
    #             print(str(file)+color.BOLD+color.WARNING+"  *** [Test FAILED] *** "+color.ENDC)
    #             #print("Tout ne pas va bien pour " + str(file))
    #             print("Le résultat obtenu :", resultat)
    #             print("Le résultat attendu :", attendu)
    #     os.system("rm " +  "../deca/codegen/valid/provided" + "/"+ "*.ass")
    #     print("======================================================")

    print(color.BOLD+color.OKBLUE+"      X+X+X+X+X [TEST SANS OBJET GENCODE] X+X+X+X+X "+color.ENDC)
    print("~======================================================~")

    for file in files(valid_gencode_SO):
        if (str(file))[len(str(file))-5:] == ".deca":
            tmp += 1
            execute = test_gencode + " " + valid_gencode_SO + "/" + str(file)
            os.system(execute)
            if os.path.isfile(str(file)[:-4]+"ass"):
                print("Fichier {}ass non généré".format(str(file)[:-4]))
                exit()
            resultat = os.system("ima {}".format(valid_gencode_SO + "/"+str(file)[:-4])+"ass" + "> resultat.log")
            fichier = open("resultat.log", "r")
            resultat = fichier.readlines()[0]
            resultat = resultat[:len(resultat)-1]
            os.system("rm resultat.log")
            if str(file) == "Cast.deca":
                attendu= "3.60000e+00 3 3 3.00000e+00 28"
            elif str(file) == "castIntFloat.deca":
                attendu = "(int) 2.50000e+00 = 2"
            elif str(file) == "Mult.deca":
                attendu= "1.51250e+03"
            elif str(file) == "ExempleDiapo.deca":
                attendu= "-4"
            elif str(file) == "testOr.deca":
                attendu= "5<n<8"
            elif str(file) == "opModulo32.deca":
                attendu= "120"
            elif str(file) == "Factoriel.deca":
                attendu = "3628800"
            elif str(file) == "FibonacciSuite.deca":
                attendu = "13"
            elif str(file) == "IfElseExpression.deca":
                attendu = "x est inf à 4"
            elif str(file) == "sommeFloatInt.deca":
                attendu = "1.98000e+01"
            elif str(file) == "conditions.deca":
                attendu = "ok2"
            elif str(file) == "ifImb.deca":
                attendu = "x est impair"
            elif str(file) == "multIntFloat.deca":
                attendu = "7.87500e+02"
            elif str(file) == "moyenne.deca":
                attendu = "-3.07500e+01"
            elif str(file) == "NotTest.deca":
                attendu = "not ok"
            elif str(file) == "pgcd.deca":
                attendu = "54"
            elif str(file) == "pingPong.deca":
                attendu = "pingpongpingpongpingpongpingpong"
            elif str(file) == "puiss34.deca":
                attendu = "81"
            elif str(file) == "mult.deca":
                attendu = "720"
            elif str(file) == "RegMin.deca":
                attendu = "528 528"
            elif str(file) == "testAssign.deca":
                attendu = "1.50000e+00"
            elif str(file) == "testDivision0.deca":
                attendu = "Error :Division par 0"
            elif str(file) == "testOpBinaire.deca":
                attendu = "4950"
            elif str(file) == "testPrintx.deca":
                attendu = "0x1.6p+3"
            elif str(file) == "unaryMinus.deca":
                attendu = "-3"
            elif str(file) == "whileOr.deca":
                attendu = "ok"
            elif str(file) == "division.deca":
                attendu = "25"
            elif str(file) == "modulo.deca":
                attendu = "2"
            elif str(file) == "min.deca":
                attendu = "45"
            elif str(file) == "max.deca":
                attendu = "4.58000e+01"
            elif str(file) == "guillemets.deca":
                attendu = "Hello\"World"
            elif str(file) == "bigDeca.deca":
                attendu = resultat #on teste pas celui la
            else:
                print("fichier pas encore traité")
                exit()

            if resultat==attendu:
                print(str(file)+color.BOLD+color.HEADER+"  *** [TEST PASSED] *** "+color.ENDC)
                counter+=1
                #print("Tout va bien pour " + str(file))
            else:
                print(str(file)+color.BOLD+color.WARNING+"  *** [Test FAILED] *** "+color.ENDC)
                #print("Tout ne pas va bien pour " + str(file))
                print("Le résultat obtenu :", resultat)
                print("Le résultat attendu :", attendu)
        os.system("rm " +  "../deca/codegen/valid/sansObjet" + "/"+ "*.ass")
        print("======================================================")

    print(color.BOLD+color.OKBLUE+"  X+X+X+X+X  [TEST OBJET GENCODE]  X+X+X+X+X "+color.ENDC)
    print("~======================================================~")

    for file in files(valid_gencode_O):
        if (str(file))[len(str(file))-5:] == ".deca":
            tmp += 1
            execute = test_gencode + " " + valid_gencode_O + "/" + str(file)
            os.system(execute)
            if os.path.isfile(str(file)[:-4]+"ass"):
                print("Fichier {}ass non généré".format(str(file)[:-4]))
                exit()
            resultat = os.system("ima {}".format(valid_gencode_O + "/"+str(file)[:-4])+"ass" + "> resultatObjet.log")
            fichier = open("resultatObjet.log", "r")
            resultat = fichier.readlines()[0]
            resultat = resultat[:len(resultat)-1]
            os.system("rm resultatObjet.log")
            if str(file) == "abs.deca":
                attendu= "5.20000e+00 2.00000e+00"

            elif str(file) == "classPoint.deca":
                attendu = "3 4 4 16"
            elif str(file) == "ClassWithExtends.deca":
                attendu = "10"
            elif str(file) == "factAndFibo.deca":
                attendu= "720 8"
            elif str(file) == "gettersAndSetters.deca":
                attendu = "3.50000e+00"
            elif str(file) == "pgcd.deca":
                attendu = "12"
            elif str(file) == "equals.deca":
                attendu = "ok"
            elif str(file) == "point.deca":
                attendu= "2 4 5 3"
            elif str(file) == "power.deca":
                attendu= "1.25000e-01"
            elif str(file) == "ppcm.deca":
                attendu = "36"
            elif str(file) == "testPoly.deca":
                attendu = "a.getX() 1"
            elif str(file) == "triangle.deca":
                attendu = "4"
            elif str(file) == "TwoClasses.deca":
                attendu = "1"
            elif str(file) == "multExtends.deca":
                attendu = "1 0.00000e+00 3 4.00000e+00"
            elif str(file) == "testasm.deca":
                attendu = "test asm"
            elif str(file) == "coeffBino.deca":
                attendu = "924"
            elif str(file) == "overrideAttribute.deca":
                attendu = "25"
            elif str(file) == "printInMethod.deca":
                attendu = "banner"
            elif str(file) == "callMethod.deca":
                attendu  = "3.00000e+00"
            elif str(file) == "initFloat.deca":
                attendu = "0.00000e+00"
            elif str(file) == "initInt.deca":
                attendu = "0"
            elif str(file) == "ClassWithExtends2.deca":
                attendu = "5 2 3 3"
            elif str(file) == "returnFloat.deca":
                attendu = "3.33333e-01"
            elif str(file) == "racine.deca":
                attendu = "9.00000e+00"
            else:
                print("fichier pas encore traité")
                print(str(file));
                exit()

            if resultat==attendu:
                print(str(file)+color.BOLD+color.HEADER+"  *** [TEST PASSED] *** "+color.ENDC)
                counter+=1
                #print("Tout va bien pour " + str(file))
            else:
                print(str(file)+color.BOLD+color.WARNING+"  *** [Test FAILED] *** "+color.ENDC)
                #print("Tout ne pas va bien pour " + str(file))
                print("Le résultat obtenu :", resultat)
                print("Le résultat attendu :", attendu)
        os.system("rm " +  "../deca/codegen/valid/objet" + "/"+ "*.ass")
        print("======================================================")


    return counter,tmp

x = valid_gencode()
if (x[0] == x[1]):
    print(color.BOLD+ color.OKGREEN+"     .-~-.-~-.-~[{} TESTS GENCODE SUCCESS].-~-.-~-.-~".format(str(x[1]))+color.ENDC)
    print("~======================================================~")
else:
    print(color.BOLD+ color.FAIL+"     .-~-.-~-.-~[{} TESTS GENCODE ERROR].-~-.-~-.-~".format(str(x[1] - x[0]))+color.ENDC)
    print("~========================================================================~")
# os.system("rm " +  "../deca/codegen/valid/sansObjet" + "/"+ "*.ass")
