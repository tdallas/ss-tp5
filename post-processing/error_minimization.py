import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
import math
from parser_xyz import XYZParser


def get_experimental_velocity(experimental_velocities, experimental_densities, density_to_find):
    range = 0.25
    velocities = []
    i = 0
    while i < len(experimental_densities) and experimental_densities[i] <= (density_to_find + range):
        if experimental_densities[i] >= (density_to_find - range):
            velocities.append(experimental_velocities[i])
        i += 1
    if len(velocities) > 0:
        return np.average(velocities)
    else:
        return None

def parse_quadratic_errors(parameters_string, from_number, to_number, variable_jump, max_particles, particles_jump, inner_radius, outer_radius):
    predtchenskii_milinskii = pd.read_csv('data/predtchenskii-milinskii.csv')
    experimental_densities = predtchenskii_milinskii['density']
    experimental_velocities = predtchenskii_milinskii['velocity']

    errors = []
    variables = []
    while from_number <= to_number:
        print('Processing ' + str(from_number) + ' radius:')
        error = 0
        variables.append(from_number*0.01)
        string = parameters_string + str(from_number)
        average_velocities, average_velocities_error_bars, densities = parse_fundamental_diagram_data(string, max_particles, particles_jump, inner_radius, outer_radius)
        i = 0
        n = 0
        while i < len(densities):
            density = densities[i]
            experimental_velocity = get_experimental_velocity(experimental_velocities, experimental_densities, density)
            if experimental_velocity is not None:
                error += (average_velocities[i] - experimental_velocity)**2
                n += 1
            i += 1
        print('Error ' + str(error/n) + ' with ' + str(from_number) + ' radius.')
        errors.append(error/n)
        from_number += variable_jump
    return errors, variables


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
    densities.append(3 / area)

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

def plot_experimental_comparison(label_string, average_velocities, average_velocities_error_bars, densities):

    predtchenskii_milinskii = pd.read_csv('data/predtchenskii-milinskii.csv')
    plt.scatter(predtchenskii_milinskii['density'], predtchenskii_milinskii['velocity'], marker='v', facecolors='none', edgecolors='g', label='Predtchenskii And Milinskii')

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


inner_radius = 2
outer_radius = 5
max_particles = 480
particles_jump = 24
from_number = 27
to_number = 42
variable_jump = 1
parameters_string = 'p2-circle-rmax-'
errors, variables = parse_quadratic_errors(parameters_string, from_number, to_number, variable_jump, max_particles, particles_jump, inner_radius, outer_radius)

plt.scatter(variables, errors)
plt.plot(variables, errors)
plt.xlabel('Radio máximo', fontsize=16)
plt.ylabel('Error cuadrático medio', fontsize=16)
plt.title('Minimización del error')
plt.tight_layout()
plt.show()

print('Parsing with minimum error')
inner_radius = 2
outer_radius = 5
max_particles = 480
particles_jump = 24
parameters_string = 'p2-circle-rmax-32'
label_string_p2 = 'Parámetros 2'
average_velocities_p2, average_velocities_error_bars_p2, densities_p2 = parse_fundamental_diagram_data(parameters_string, max_particles, particles_jump, inner_radius, outer_radius)
i = 0
n = 0
predtchenskii_milinskii = pd.read_csv('data/predtchenskii-milinskii.csv')
experimental_densities = predtchenskii_milinskii['density']
experimental_velocities = predtchenskii_milinskii['velocity']
error = 0
while i < len(densities_p2):
    density = densities_p2[i]
    experimental_velocity = get_experimental_velocity(experimental_velocities, experimental_densities, density)
    if experimental_velocity is not None:
        error += (average_velocities_p2[i] - experimental_velocity)**2
        n += 1
    i += 1
ecm = error/n
print('ECM ' + str(ecm) + ' with 0.32 maximum radius.')
plot_experimental_comparison(label_string_p2, average_velocities_p2, average_velocities_error_bars_p2, densities_p2)
