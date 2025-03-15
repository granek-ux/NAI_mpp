from Operations import Obserwacja


def file_read(filename):
    lista_obserwacji = []
    file = open(filename, 'r')
    for line in file:
        line = line.strip()
        line = line.replace(',', '.')
        split = line.split()
        lista_atrybuty = []
        for i in range(len(split) - 1):
            lista_atrybuty.append(float(split[i]))
        obs = Obserwacja(split[len(split) - 1], lista_atrybuty)
        lista_obserwacji.append(obs)
    file.close()
    return lista_obserwacji
