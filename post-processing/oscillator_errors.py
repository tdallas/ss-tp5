import pandas as pd
import matplotlib.pyplot as plt
from sklearn.metrics import mean_squared_error
from matplotlib.ticker import ScalarFormatter
"LITTLE DTS"
dts = ['0.0001', '0.0002', '0.0003', '0.0004', '0.0005', '0.0006', '0.0007', '0.0008', '0.0009', '0.0010']

y_lims = {'beeman': [0.0002491712821944489, 0.0002526572314244125], 'gear': [
    0.0002611022367250463, 0.00036243732110377983], 'euler': [0.0002216100069899247, 0.0002406905542996367]}

oscilators = ['beeman', 'gear', 'euler']

errors_for_oscilators = {'beeman': [],
                         'gear': [],
                         'euler': []}
for oscilator_type in oscilators:
    errors = []
    for dt in dts:
        oscilator = pd.read_csv("out/oscillator-analytic-{}.csv".format(dt))
        oscilator_analytics_position = oscilator['position']

        oscilator_to_cmp = pd.read_csv("out/oscillator-{}-{}.csv".format(oscilator_type, dt))
        oscilator_to_cmp_position = oscilator_to_cmp['position']

        errors.append(mean_squared_error(
            oscilator_analytics_position, oscilator_to_cmp_position))

    plt.scatter(list(map(lambda dt: float(dt), dts)), errors,
                label='Error cuadrático para ' + oscilator_type)
    plt.plot(list(map(lambda dt: float(dt), dts)), errors,
                label='Error cuadrático para ' + oscilator_type)
    plt.xlabel('Dt [S]', fontsize=16)
    plt.ylabel('Error cuadrático', fontsize=16)
    # plt.ylim(y_lims[oscilator_type][0], y_lims[oscilator_type][1])

    plt.yscale('log')
    plt.xscale('log')

    ax = plt.gca()
    ax.set_xticks(list(map(lambda dt: float(dt), dts))[1:]) # note that with a log axis, you can't have x = 0 so that value isn't plotted.
    # ax.set_yticks(errors[1:]) # note that with a log axis, you can't have x = 0 so that value isn't plotted.
    
    ax.xaxis.set_major_formatter(ScalarFormatter())    # plt.ticklabel_format(axis="x", style="sci",
    ax.yaxis.set_major_formatter(ScalarFormatter())    
    
    plt.ticklabel_format(axis="x", style="sci",
                         scilimits=(-4, -4), useMathText=True)
    # plt.ticklabel_format(axis="y", style="sci",
    #                      scilimits=(-4, -4), useMathText=True)
    
    plt.title('Error cuadrático para: ' + oscilator_type)
    plt.tight_layout()
    plt.show()
    errors_for_oscilators[oscilator_type] = errors