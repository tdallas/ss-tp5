import matplotlib.pyplot as plt
from parser_xyz import XYZParser

min_distances = []
days = []

for i in range(60):
    parsed_data = XYZParser("out/spaceship-jupiter-204-day-12-hour-{}-minute-v0.xyz".format(i))
    min_distances.append(parsed_data.get_min_distance_between_particles(3, 4))
    days.append(i)

print('Mejor distancia alcanzada a Jupiter en 204 dias, 12 horas y 24 minutos: ' + str(min_distances[24]) + ' metros')

plt.plot(days, min_distances)
plt.scatter(days, min_distances)
plt.xlabel('Minuto', fontsize=16)
plt.ylabel('Distancia [M]', fontsize=16)
plt.ticklabel_format(axis="x", style="sci", useMathText=True)
plt.ticklabel_format(axis="y", style="sci", useMathText=True)
plt.title('Mínima distancia de la nave a Jupiter en día 204 y hora 12')
plt.tight_layout()
plt.show()