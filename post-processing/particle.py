from math import sqrt


class Particle:
    def __init__(self, id, x_position, y_position, x_velocity, y_velocity, radius, mass, time_passed):
        self.id = int(id)
        self.x_position = float(x_position)
        self.y_position = float(y_position)
        self.x_velocity = float(x_velocity)
        self.y_velocity = float(y_velocity)
        self.radius = float(radius)
        self.mass = float(mass)
        self.time_passed = float(time_passed)

    def __str__(self):
        return self.x_position + ' | ' + self.y_position

    def get_x_position(self):
        return self.x_position

    def get_y_position(self):
        return self.y_position

    def get_x_velocity(self):
        return self.x_velocity

    def get_y_velocity(self):
        return self.y_velocity

    def get_velocity(self):
        return sqrt((self.get_x_velocity() ** 2) + (self.get_y_velocity()**2))

    def get_id(self):
        return self.id

    def get_mass(self):
        return self.mass

    def get_radius(self):
        return self.radius

    def get_distance(self, particle):
        return sqrt((self.x_position - particle.x_position)**2 + (self.y_position - particle.y_position)**2) - self.radius - particle.radius

    def get_time_passed(self):
        return self.time_passed
