import numpy as np
import matplotlib.pyplot as plt
import math
import sys
from parser_xyz import XYZParser

min_radius = 0.15
max_radius = 0.32
radius = 4
length = radius * 2 * math.pi
width_multiplier = 3
width = width_multiplier * (max_radius + min_radius)
area = length * width
max_particles = 160
particles_jump = 10

average_velocities = []
average_velocities_error_bars = []
densities = []

for particles_quantity in range(particles_jump, max_particles + 1, particles_jump):
    print(str(particles_quantity) + ' particles')
    file = "out/width-{}-particles-{}.xyz".format(width_multiplier, particles_quantity)
    parsed_data = XYZParser(file)
    output = parsed_data.get_output()
    average_velocities_iteration = []
    for i in range(249, 1250):
        iteration = output[i]
        velocities = []
        for particle in iteration:
            velocities.append(particle.get_velocity())
        average_velocities_iteration.append(np.average(velocities))
    average_velocities.append(np.average(average_velocities_iteration))
    average_velocities_error_bars.append(np.std(average_velocities_iteration))
    densities.append(particles_quantity / area)



plt.errorbar(densities, average_velocities, yerr=average_velocities_error_bars, capsize=3, elinewidth=1, markeredgewidth=1)
plt.scatter(densities, average_velocities)
plt.plot(densities, average_velocities)
plt.xlabel('Densidad [1/m²]', fontsize=16)
plt.ylabel('Velocidad Promedio [m/s]', fontsize=16)
plt.title('Diagrama Fundamental')
plt.tight_layout()
plt.show()
