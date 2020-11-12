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
        if(len(velocities) > 0):
            average_velocities.append(np.average(velocities))
        else:
            time.pop()

    return average_velocities, time

def plot_average_velocity(average_velocities, time):
    plt.plot(time, average_velocities)
    plt.xlabel('Tiempo [s]', fontsize=16)
    plt.ylabel('Velocidad [m/s]', fontsize=16)
    plt.title('Velocidad promedio')
    plt.ylim(0, 1.65)
    plt.tight_layout()
    plt.show()

file = 'out/p1-circle-width-3.0-particles-48.xyz'
average_velocities_1, time_1 = parse_average_velocity(file)

file = 'out/p1-circle-width-3.0-particles-120.xyz'
average_velocities_2, time_2 = parse_average_velocity(file)

file = 'out/p1-circle-width-3.0-particles-216.xyz'
average_velocities_3, time_3 = parse_average_velocity(file)

file = 'out/p1-circle-width-3.0-particles-312.xyz'
average_velocities_4, time_4 = parse_average_velocity(file)

file = 'out/p1-circle-width-3.0-particles-408.xyz'
average_velocities_5, time_5 = parse_average_velocity(file)

file = 'out/p1-circle-width-3.0-particles-480.xyz'
average_velocities_6, time_6 = parse_average_velocity(file)

plt.plot(time_1, average_velocities_1, label='48 partículas')
plt.plot(time_2, average_velocities_2, label='120 partículas')
plt.plot(time_3, average_velocities_3, label='216 partículas')
plt.plot(time_4, average_velocities_4, label='312 partículas')
plt.plot(time_5, average_velocities_5, label='408 partículas')
plt.plot(time_6, average_velocities_6, label='480 partículas')
plt.xlabel('Tiempo [s]', fontsize=16)
plt.ylabel('Velocidad [m/s]', fontsize=16)
plt.title('Velocidad promedio')
plt.legend(title='Cantidad de partículas')
plt.ylim(0, 1.65)
plt.xlim(-5, 150)
plt.tight_layout()
plt.show()