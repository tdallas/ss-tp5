import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
import math
from parser_xyz import XYZParser


def parse_fundamental_diagram_data(parameters_string, max_particles, particles_jump, inner_radius, outer_radius):
    area = math.pi * (outer_radius**2 - inner_radius**2)
    width = outer_radius - inner_radius
    average_velocities = []
    average_velocities_error_bars = []
    densities = []

    for particles_quantity in range(particles_jump, max_particles + 1, particles_jump):
        print('Processing ' + str(particles_quantity) + ' particles...')
        file = "out/{}-width-{}-particles-{}.xyz".format(parameters_string, str(float(width)), particles_quantity)
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

def plot_experimental_comparison(label_string, average_velocities, average_velocities_error_bars, densities):
    helbing = pd.read_csv('data/helbing.csv')
    plt.scatter(helbing['density'], helbing['velocity'], marker='s', facecolors='none', edgecolors='r', label='Helbing')
    mori_tsukaguchi = pd.read_csv('data/mori-tsukaguchi.csv')
    plt.scatter(mori_tsukaguchi['density'], mori_tsukaguchi['velocity'], marker='^', facecolors='none', edgecolors='b', label='Mori And Tsukaguchi')
    predtchenskii_milinskii = pd.read_csv('data/predtchenskii-milinskii.csv')
    plt.scatter(predtchenskii_milinskii['density'], predtchenskii_milinskii['velocity'], marker='v', facecolors='none', edgecolors='g', label='Predtchenskii And Milinskii')
    weidmann = pd.read_csv('data/weidmann.csv')
    plt.scatter(weidmann['density'], weidmann['velocity'], marker='d', facecolors='none', edgecolors='y', label='Weidmann')

    plt.errorbar(densities, average_velocities, yerr=average_velocities_error_bars, capsize=3, elinewidth=1, markeredgewidth=1)
    plt.scatter(densities, average_velocities, label=label_string)
    plt.plot(densities, average_velocities)

    plt.xlabel('Densidad [1/m²]', fontsize=16)
    plt.ylabel('Velocidad Promedio [m/s]', fontsize=16)
    plt.title('Comparación con diagramas fundamentales')
    plt.legend(title='Diagrama Fundamental')
    plt.ylim(0, 1.65)
    plt.tight_layout()
    plt.show()
    

print('Parsing with parameters 1')
inner_radius = 2
outer_radius = 5
max_particles = 480
particles_jump = 24
parameters_string = 'p1-circle'
label_string_p1 = 'Parámetros 1'
average_velocities_p1, average_velocities_error_bars_p1, densities_p1 = parse_fundamental_diagram_data(parameters_string, max_particles, particles_jump, inner_radius, outer_radius)
plot_experimental_comparison(label_string_p1, average_velocities_p1, average_velocities_error_bars_p1, densities_p1)

print('Parsing with parameters 2')
inner_radius = 2
outer_radius = 5
max_particles = 480
particles_jump = 24
parameters_string = 'p2-circle'
label_string_p2 = 'Parámetros 2'
average_velocities_p2, average_velocities_error_bars_p2, densities_p2 = parse_fundamental_diagram_data(parameters_string, max_particles, particles_jump, inner_radius, outer_radius)
plot_experimental_comparison(label_string_p2, average_velocities_p2, average_velocities_error_bars_p2, densities_p2)

