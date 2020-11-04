import matplotlib.pyplot as plt
from parser_xyz import XYZParser

parsed_data = XYZParser('out/0-spaceship-to-mars.xyz')

velocity = []
time = []

index_spaceship_launch, spaceship = parsed_data.get_particle_with_id(3)
time_before_spaceship_launch = parsed_data.get_output()[index_spaceship_launch][0].get_time_passed()
for i in range(1, len(spaceship) - 1):
    velocity.append(spaceship[i].get_velocity())
    time.append(spaceship[i].get_time_passed() - parsed_data.get_output()[index_spaceship_launch][0].get_time_passed())

print('Tiempo de viaje total a Marte: ' + str(time[len(time) - 1]) + ' segundos')
print('Velocidad con la que lleua la nave a Marte: ' + str(velocity[len(velocity) - 1]) + ' metros/segundos')

plt.plot(time, velocity)
plt.xlabel('Tiempo [S]', fontsize=16)
plt.ylabel('Velocidad [M/S]', fontsize=16)
plt.ticklabel_format(axis="x", style="sci", useMathText=True)
plt.ticklabel_format(axis="y", style="sci", useMathText=True)
plt.title('Velocidad de la nave hasta distancia mínima a Marte')
plt.tight_layout()
plt.show()