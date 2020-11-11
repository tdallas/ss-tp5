import numpy as np
import matplotlib.pyplot as plt
import math
from parser_xyz import XYZParser


def parse_fundamental_diagram_data(parameters_string, max_particles, particles_jump, inner_radius, outer_radius):
    area = math.pi * (outer_radius**2 - inner_radius**2)
    width = outer_radius - inner_radius
    average_velocities = []
    average_velocities_error_bars = []
    densities = []

    print('Processing ' + str(3) + ' particles...')
    file = "out/{}-width-{}-particles-{}.xyz".format(parameters_string, str(float(width)), 3)
    parsed_data = XYZParser(file)
    output = parsed_data.get_output()
    average_velocities_iteration = []
    for i in range(int((len(output)/3)), len(output)):
        iteration = output[i]
        velocities = []
        for particle in iteration:
            if not particle.is_overlapped():
                velocities.append(particle.get_velocity())
        if(len(velocities) > 0):
            average_velocities_iteration.append(np.average(velocities))
    average_velocities.append(np.average(average_velocities_iteration))
    average_velocities_error_bars.append(np.std(average_velocities_iteration))
    densities.append(1 / area)

    for particles_quantity in range(particles_jump, max_particles + 1, particles_jump):
        print('Processing ' + str(particles_quantity) + ' particles...')
        file = "out/{}-width-{}-particles-{}.xyz".format(parameters_string, str(float(width)), particles_quantity)
        parsed_data = XYZParser(file)
        output = parsed_data.get_output()
        average_velocities_iteration = []
        for i in range(int((len(output)/3)), len(output)):
            iteration = output[i]
            velocities = []
            for particle in iteration:
                if not particle.is_overlapped():
                    velocities.append(particle.get_velocity())
            if(len(velocities) > 0):
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
    plt.ylim(0, 1.65)
    plt.tight_layout()
    plt.show()

print('Plotting with 1 width')
inner_radius = 2
outer_radius = 3
max_particles = 105
particles_jump = 5
parameters_string = 'p1-circle'
average_velocities_1, average_velocities_error_bars_1, densities_1 = parse_fundamental_diagram_data(parameters_string, max_particles, particles_jump, inner_radius, outer_radius)
plot_fundamental_diagram(average_velocities_1, average_velocities_error_bars_1, densities_1)

print('Plotting with 1.5 width')
inner_radius = 2
outer_radius = 3.5
max_particles = 182
particles_jump = 7
parameters_string = 'p1-circle'
average_velocities_1_5, average_velocities_error_bars_1_5, densities_1_5 = parse_fundamental_diagram_data(parameters_string, max_particles, particles_jump, inner_radius, outer_radius)
plot_fundamental_diagram(average_velocities_1_5, average_velocities_error_bars_1_5, densities_1_5)

print('Plotting with 2 width')
inner_radius = 2
outer_radius = 4
max_particles = 260
particles_jump = 13
parameters_string = 'p1-circle'
average_velocities_2, average_velocities_error_bars_2, densities_2 = parse_fundamental_diagram_data(parameters_string, max_particles, particles_jump, inner_radius, outer_radius)
plot_fundamental_diagram(average_velocities_2, average_velocities_error_bars_2, densities_2)

print('Plotting with 3 width')
inner_radius = 2
outer_radius = 5
max_particles = 480
particles_jump = 24
parameters_string = 'p1-circle'
average_velocities_3, average_velocities_error_bars_3, densities_3 = parse_fundamental_diagram_data(parameters_string, max_particles, particles_jump, inner_radius, outer_radius)
plot_fundamental_diagram(average_velocities_3, average_velocities_error_bars_3, densities_3)

print('Plotting with 4 width')
inner_radius = 2
outer_radius = 6
max_particles = 700
particles_jump = 35
parameters_string = 'p1-circle'
average_velocities_4, average_velocities_error_bars_4, densities_4 = parse_fundamental_diagram_data(parameters_string, max_particles, particles_jump, inner_radius, outer_radius)
plot_fundamental_diagram(average_velocities_4, average_velocities_error_bars_4, densities_4)

print('Plotting with 5 width')
inner_radius = 2
outer_radius = 7
max_particles = 1000
particles_jump = 50
parameters_string = 'p1-circle'
average_velocities_5, average_velocities_error_bars_5, densities_5 = parse_fundamental_diagram_data(parameters_string, max_particles, particles_jump, inner_radius, outer_radius)
plot_fundamental_diagram(average_velocities_5, average_velocities_error_bars_5, densities_5)


plt.plot(densities_1, average_velocities_1, label='1 metro')
plt.plot(densities_1_5, average_velocities_1_5, label='1.5 metros')
plt.plot(densities_2, average_velocities_2, label='2 metros')
plt.plot(densities_3, average_velocities_3, label='3 metros')
plt.plot(densities_4, average_velocities_4, label='4 metros')
plt.plot(densities_5, average_velocities_5, label='5 metros')
plt.xlabel('Densidad [1/m²]', fontsize=16)
plt.ylabel('Velocidad Promedio [m/s]', fontsize=16)
plt.title('Comparación de ancho de pasillo')
plt.legend(title='Ancho de pasillo')
plt.ylim(0, 1.65)
plt.tight_layout()
plt.show()