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
        for i in range(int((len(output)/2)), len(output)):
            iteration = output[i]
            velocities = []
            for particle in iteration:
                if not particle.is_overlapped():
                    velocities.append(particle.get_velocity())
            average_velocities_iteration.append(np.average(velocities))
        average_velocities.append(np.average(average_velocities_iteration))
        average_velocities_error_bars.append(np.std(average_velocities_iteration))
        densities.append(particles_quantity / area)

    return average_velocities, average_velocities_error_bars, densities

print('Parsing with 3 width multiplier')
min_radius = 0.15
max_radius = 0.32
radius = 4
width_multiplier = 3
max_particles = 200
particles_jump = 5
parameters_string = 'p1'
average_velocities_3, average_velocities_error_bars_3, densities_3 = parse_fundamental_diagram_data(parameters_string, max_particles, particles_jump, min_radius, max_radius, radius, width_multiplier)

print('Parsing with 4 width multiplier')
min_radius = 0.15
max_radius = 0.32
radius = 4
width_multiplier = 4
max_particles = 250
particles_jump = 5
parameters_string = 'p1'
average_velocities_4, average_velocities_error_bars_4, densities_4 = parse_fundamental_diagram_data(parameters_string, max_particles, particles_jump, min_radius, max_radius, radius, width_multiplier)

print('Parsing with 5 width multiplier')
min_radius = 0.15
max_radius = 0.32
radius = 4
width_multiplier = 5
max_particles = 400
particles_jump = 10
parameters_string = 'p1'
average_velocities_5, average_velocities_error_bars_5, densities_5 = parse_fundamental_diagram_data(parameters_string, max_particles, particles_jump, min_radius, max_radius, radius, width_multiplier)

print('Parsing with 6 width multiplier')
min_radius = 0.15
max_radius = 0.32
radius = 4
width_multiplier = 6
max_particles = 460
particles_jump = 10
parameters_string = 'p1'
average_velocities_6, average_velocities_error_bars_6, densities_6 = parse_fundamental_diagram_data(parameters_string, max_particles, particles_jump, min_radius, max_radius, radius, width_multiplier)

print('Parsing with 7 width multiplier')
min_radius = 0.15
max_radius = 0.32
radius = 4
width_multiplier = 7
max_particles = 500
particles_jump = 20
parameters_string = 'p1'
average_velocities_7, average_velocities_error_bars_7, densities_7 = parse_fundamental_diagram_data(parameters_string, max_particles, particles_jump, min_radius, max_radius, radius, width_multiplier)

print('Parsing with 8 width multiplier')
min_radius = 0.15
max_radius = 0.32
radius = 4
width_multiplier = 8
max_particles = 560
particles_jump = 20
parameters_string = 'p1'
average_velocities_8, average_velocities_error_bars_8, densities_8 = parse_fundamental_diagram_data(parameters_string, max_particles, particles_jump, min_radius, max_radius, radius, width_multiplier)


plt.plot(densities_3, average_velocities_3, label='3*(maxR + minR)')
plt.plot(densities_4, average_velocities_4, label='4*(maxR + minR)')
plt.plot(densities_5, average_velocities_5, label='5*(maxR + minR)')
plt.plot(densities_6, average_velocities_6, label='6*(maxR + minR)')
plt.plot(densities_7, average_velocities_7, label='7*(maxR + minR)')
plt.plot(densities_8, average_velocities_8, label='8*(maxR + minR)')
plt.xlabel('Densidad [1/m²]', fontsize=16)
plt.ylabel('Velocidad Promedio [m/s]', fontsize=16)
plt.title('Comparación de ancho de pasillo')
plt.legend(title='Ancho de pasillo')
plt.ylim(0, 1.65)
plt.tight_layout()
plt.show()