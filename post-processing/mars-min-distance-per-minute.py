import matplotlib.pyplot as plt
from parser_xyz import XYZParser

min_distances = []
days = []

for i in range(60):
    parsed_data = XYZParser("out/spaceship-709-day-12-hour-{}-minute-v0.xyz".format(i))
    min_distances.append(parsed_data.get_min_distance_between_particles(2, 3))
    days.append(i)

print('Mejor distancia alcanzada a Marte en 709 dias, 12 horas y 47 minutos: ' + str(min_distances[47]) + ' metros')

plt.plot(days, min_distances)
plt.scatter(days, min_distances)
plt.xlabel('Minuto', fontsize=16)
plt.ylabel('Distancia [M]', fontsize=16)
plt.ticklabel_format(axis="x", style="sci", useMathText=True)
plt.ticklabel_format(axis="y", style="sci", useMathText=True)
plt.title('Mínima distancia de la nave a Marte en día 709 y hora 12')
plt.tight_layout()
plt.show()