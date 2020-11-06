import numpy as np
import matplotlib.pyplot as plt
from parser_xyz import XYZParser


file = 'out/output.xyz'
parsed_data = XYZParser(file)
average_velocities = []
time = []

for iteration in parsed_data.get_output():
    velocities = []
    time.append(iteration[0].get_time_passed())
    for particle in iteration:
        velocities.append(particle.get_velocity())
    average_velocities.append(np.average(velocities))

plt.plot(time, average_velocities)
plt.xlabel('Tiempo [s]', fontsize=16)
plt.ylabel('Velocidad [m/s]', fontsize=16)
plt.title('Velocidad promedio')
plt.ylim(0, 1.55)
plt.tight_layout()
plt.show()
