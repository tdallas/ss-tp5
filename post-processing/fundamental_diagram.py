import numpy as np
import matplotlib.pyplot as plt
import math
from parser_xyz import XYZParser


def parse_fundamental_diagram_data(parameters_string, max_particles, particles_jump, min_radius, max_radius, radius, width_multiplier):
    length = radius * 2 * math.pi
    width = width_multiplier * (max_radius + min_radius)
    area = length * width
    average_velocities = []
    average_velocities_error_bars = []
    densities = []

    for particles_quantity in range(particles_jump, max_particles + 1, particles_jump):
        print('Processing ' + str(particles_quantity) + ' particles...')
        file = "out/{}-width-{}-particles-{}.xyz".format(parameters_string, width_multiplier, particles_quantity)
        parsed_data = XYZParser(file)
        output = parsed_data.get_output()
        average_velocities_iteration = []
        for i in range(int((len(output)/3)), len(output)):
            iteration = output[i]
            velocities = []
            for particle in iteration:
                velocities.append(particle.get_velocity())
            average_velocities_iteration.append(np.average(velocities))
        average_velocities.append(np.average(average_velocities_iteration))
        average_velocities_error_bars.append(np.std(average_velocities_iteration))
        densities.append(particles_quantity / area)

    return average_velocities, average_velocities_error_bars, densities

def plot_fundamental_diagram(average_velocities, average_velocities_error_bars, densities):
    plt.errorbar(densities, average_velocities, yerr=average_velocities_error_bars, capsize=3, elinewidth=1, markeredgewidth=1)
    plt.scatter(densities, average_velocities)
    plt.plot(densities, average_velocities)
    plt.xlabel('Densidad [1/m²]', fontsize=16)
    plt.ylabel('Velocidad Promedio [m/s]', fontsize=16)
    plt.title('Diagrama Fundamental')
    plt.tight_layout()
    plt.show()

print('Plotting with 3 width multiplier')
min_radius = 0.15
max_radius = 0.32
radius = 4
width_multiplier = 3
max_particles = 200
particles_jump = 5
parameters_string = 'p1'
average_velocities, average_velocities_error_bars, densities = parse_fundamental_diagram_data(parameters_string, max_particles, particles_jump, min_radius, max_radius, radius, width_multiplier)
plot_fundamental_diagram(average_velocities, average_velocities_error_bars, densities)

print('Plotting with 5 width multiplier')
min_radius = 0.15
max_radius = 0.32
radius = 4
width_multiplier = 5
max_particles = 400
particles_jump = 10
parameters_string = 'p1'
average_velocities, average_velocities_error_bars, densities = parse_fundamental_diagram_data(parameters_string, max_particles, particles_jump, min_radius, max_radius, radius, width_multiplier)
plot_fundamental_diagram(average_velocities, average_velocities_error_bars, densities)

print('Plotting with 7 width multiplier')
min_radius = 0.15
max_radius = 0.32
radius = 4
width_multiplier = 7
max_particles = 500
particles_jump = 20
parameters_string = 'p1'
average_velocities, average_velocities_error_bars, densities = parse_fundamental_diagram_data(parameters_string, max_particles, particles_jump, min_radius, max_radius, radius, width_multiplier)
plot_fundamental_diagram(average_velocities, average_velocities_error_bars, densities)
