import Utilites
from Utilites import *
from Operations import *

print('podaj k: ')
k = int(input())

porwnania = Porownania('iris_training.txt', 'iris_test.txt', k)

porwnania.test()

dlugosc = len( porwnania.lista_traning[0].atrybuty_warunkowe)

while True:
    print('podaj wektor: ')
    lista_wektor = []
    for i in range(dlugosc):
        lista_wektor.append( float(input()))

    obsymp = Obserwacja('', lista_wektor)

    print(porwnania.getNameForOneVector(obsymp))
