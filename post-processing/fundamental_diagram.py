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
max_particles = 100
particles_jump = 5
repetitions = 5

average_velocities = []
average_velocities_error_bars = []
densities = []

for particles_quantity in range(5, max_particles + 1, particles_jump):
    print(str(particles_quantity) + ' particles:', end=" ")
    velocities = []
    for repetition in range(1, repetitions + 1):
        print(str(repetition), end=" ")
        sys.stdout.flush()
        file = "out/width-{}-particles-{}-repetition-{}.xyz".format(width_multiplier, particles_quantity, repetition)
        parsed_data = XYZParser(file)
        for iteration in parsed_data.get_output():
            velocities_repetition = []
            for particle in iteration:
                velocities_repetition.append(particle.get_velocity())
        velocities.append(np.average(velocities_repetition))
    print()
    average_velocities.append(np.average(velocities))
    average_velocities_error_bars.append(np.std(velocities))
    densities.append(particles_quantity / area)



plt.errorbar(densities, average_velocities, yerr=average_velocities_error_bars, capsize=3, elinewidth=1, markeredgewidth=1)
plt.scatter(densities, average_velocities)
plt.plot(densities, average_velocities)
plt.xlabel('Densidad [1/m²]', fontsize=16)
plt.ylabel('Velocidad Promedio [m/s]', fontsize=16)
plt.title('Diagrama Fundamental')
plt.tight_layout()
plt.show()
