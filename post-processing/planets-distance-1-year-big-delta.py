import matplotlib.pyplot as plt
from parser_xyz import XYZParser
from math import sqrt


def point_distance(x1, y1, x2, y2):
    return sqrt((x1 - x2) ** 2 + (y1 - y2) ** 2)

earth_x_position = 1.493890685719635E11
earth_y_position = 1.252824102724684E10

earth_distances_euler = []
earth_distances_beeman = []
time_deltas = []

i = 200
while(i < 1001):
    time_deltas.append(i)
    parsed_data_beeman = XYZParser('out/planet-beeman-{}.0.xyz'.format(i))
    parsed_data_euler = XYZParser('out/planet-euler-{}.0.xyz'.format(i))

    iteration = len(parsed_data_euler.get_output()) - 1

    earth_euler = parsed_data_euler.get_particle_in_iteration(1, iteration)
    earth_beeman = parsed_data_beeman.get_particle_in_iteration(1, iteration)

    earth_distance_euler = point_distance(earth_x_position, earth_y_position, earth_euler.get_x_position(), earth_euler.get_y_position())
    earth_distance_beeman = point_distance(earth_x_position, earth_y_position, earth_beeman.get_x_position(), earth_beeman.get_y_position())

    earth_distances_euler.append(earth_distance_euler)
    earth_distances_beeman.append(earth_distance_beeman)

    i += 200

plt.plot(time_deltas, earth_distances_euler, label='Euler')
plt.plot(time_deltas, earth_distances_beeman, label='Beeman')
plt.scatter(time_deltas, earth_distances_euler)
plt.scatter(time_deltas, earth_distances_beeman)
plt.xlabel('dT [S]', fontsize=16)
plt.ylabel('Distancia [M]', fontsize=16)
plt.ticklabel_format(axis="x", style="sci", useMathText=True)
plt.ticklabel_format(axis="y", style="sci", useMathText=True)
plt.legend(title='Tipo de Integrador')
plt.title('Distancia de la tierra a posición de la nasa')
plt.tight_layout()
plt.show()