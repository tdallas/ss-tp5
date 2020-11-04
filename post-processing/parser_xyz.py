from particle import Particle


class XYZParser:
    def __init__(self, output_path):
        self.output = self.parse_output(output_path)

    def get_output(self):
        return self.output

    def parse_output(self, output_path):
        output = []
        with open(output_path) as f:
            lines = f.readlines()
            iteration = []
            for line in lines:
                if self.is_header(line):
                    pass
                elif self.iteration_finished(line):
                    if len(iteration) > 1:
                        output.append(iteration)
                    iteration = []
                else:
                    particle = self.create_particle(line.replace('\n', '').split(' '))
                    iteration.append(particle)
            output.append(iteration)
        return output

    def get_particle_with_id(self, particle_id):
        index_set = False
        index_appears_particle_with_id = 0
        particle_with_id = []
        for i in range(len(self.output)):
            for j in range(len(self.output[i])):
                if self.output[i][j].get_id() == particle_id:
                    if not index_set:
                        index_set = True
                        index_appears_particle_with_id = i - 1
                    particle_with_id.append(self.output[i][j])
        return index_appears_particle_with_id, particle_with_id

    def get_min_distance_between_particles(self, particle_id_1, particle_id_2):
        min_distance = 1.7976931348623158E+308
        for i in range(len(self.output)):
            particle_1 = self.get_particle_in_iteration(particle_id_1, i)
            particle_2 = self.get_particle_in_iteration(particle_id_2, i)
            if particle_1 is not None and particle_2 is not None:
                distance = particle_1.get_distance(particle_2)
                if min_distance > distance:
                    min_distance = distance
        return min_distance

    def get_particle_in_iteration(self, particle_id, iteration):
        for i in range(len(self.output[iteration])):
            if self.output[iteration][i].get_id() == particle_id:
                return self.output[iteration][i]
        return None
    
    @staticmethod
    def is_header(line):
        return line == 'id xPosition yPosition xVelocity yVelocity radius mass animationRadius redColor greenColor blueColor timePassed\n'

    def iteration_finished(self, line):
        return len(line.split(' ')) == 1

    @staticmethod
    def create_particle(line):
        return Particle(line[0], line[1], line[2], line[3], line[4], line[5], line[6], line[11])
