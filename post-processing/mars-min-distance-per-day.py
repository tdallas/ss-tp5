import matplotlib.pyplot as plt
from parser_xyz import XYZParser

min_distances = []
days = []

for i in range(750):
    parsed_data = XYZParser("out/spaceship-{}-day-v0.xyz".format(i))
    min_distances.append(parsed_data.get_min_distance_between_particles(2, 3))
    days.append(i)

plt.plot(days, min_distances)
plt.xlabel('Día', fontsize=16)
plt.ylabel('Distancia [M]', fontsize=16)
plt.ticklabel_format(axis="x", style="sci", useMathText=True)
plt.ticklabel_format(axis="y", style="sci", useMathText=True)
plt.title('Mínima distancia de la nave a Marte')
plt.tight_layout()
plt.show()