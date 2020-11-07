import numpy as np
import matplotlib.pyplot as plt
from parser_xyz import XYZParser


def parse_average_velocity(file):
    parsed_data = XYZParser(file)
    average_velocities = []
    time = []

    for iteration in parsed_data.get_output():
        velocities = []
        time.append(iteration[0].get_time_passed())
        for particle in iteration:
            if not particle.is_overlapped():
                velocities.append(particle.get_velocity())
        average_velocities.append(np.average(velocities))

    return average_velocities, time

def plot_average_velocity(average_velocities, time, max_velocity):
    plt.plot(time, average_velocities)
    plt.xlabel('Tiempo [s]', fontsize=16)
    plt.ylabel('Velocidad [m/s]', fontsize=16)
    plt.title('Velocidad promedio')
    plt.ylim(0, max_velocity)
    plt.tight_layout()
    plt.show()

file = 'out/test.xyz'
max_velocity = 1.55
average_velocities, time = parse_average_velocity(file)
plot_average_velocity(average_velocities, time, max_velocity)