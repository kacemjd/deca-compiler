#!/usr/bin/env python3
from os import system
def executer():
    """
    generer
    """
    for i in range(100):
        system("./build.py > valid/test{}.deca".format(i))

executer()
