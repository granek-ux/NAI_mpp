import Utilites
import math
from Utilites import *

class NamedDistance:
    def __init__(self, name, distance):
        self.distance = distance
        self.name = name

class Obserwacja:
    def __init__(self, atrybutglowny, atrybuty_warunkowe):
        self.atrybuty_warunkowe = atrybuty_warunkowe
        self.atrybutglowny = atrybutglowny


class Porownania:
    def __init__(self,filename_traning, file_name_test, ksasiedzi):
        self.lista_traning = Utilites.file_read(filename_traning)
        self.__file_name_test = file_name_test
        self.__ksasiedzi = ksasiedzi

    def test(self):
        lista_test = Utilites.file_read(self.__file_name_test)
        ilosc_git =0
        ilosc_niegit =0
        for obser_test in lista_test:

            name = self.getNameForOneVector(obser_test)

            print(name + " vsvs " + obser_test.atrybutglowny)

            if name == obser_test.atrybutglowny:
                ilosc_git += 1
            else:
                ilosc_niegit += 1

        procent = ilosc_git / (ilosc_git+ilosc_niegit) * 100
        print("git: " + str(ilosc_git) + ' nie git ' + str(ilosc_niegit) + ' procent ' + str(procent) + '%' )

    def getNameForOneVector (self,obserwacja_nowa):
        distances = []
        for obs_test in self.lista_traning:
            dis = CalcuateDistance(obs_test.atrybuty_warunkowe, obserwacja_nowa.atrybuty_warunkowe)
            ndis = NamedDistance(obs_test.atrybutglowny, dis)
            distances.append(ndis)

        mapa = {}
        for i in range(self.__ksasiedzi):
            min_item = min(distances, key=lambda d: d.distance)
            mapa[min_item.name] = min_item.distance
            distances.remove(min_item)

        key = max(mapa, key=mapa.get)

        return key


def CalcuateDistance (lista_trening, lista_nowy):
    distance = 0.0
    for i in range(len(lista_trening)):
        distance += (lista_trening[i] - lista_nowy[i])**2
    distance = math.sqrt(distance)
    return distance