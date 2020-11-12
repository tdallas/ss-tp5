# ss-tp5
# Engine and simulation
## Generate executable
`mvn clean install`

## Execute command to generate all simulations

`java -jar target/ss-tp4-1.0.jar`

## Output
The program will generate all the files with similar formats like these:

`out/FILENAME.xyz`

`out/walls-FILENAME.csv`

These can be used to make an animation in Ovito.

# Post processing

**In every script, the first lines contains the file that is about to beein processed, if you want to graphic some custom behaviour, just modified the file path to match your custom file**

## Average Velocities over time
To generate graphics, run: \
`python3 post-processing/average_velocities.py`

## Fundamental Diagram Width Comparison
To generate graphics, run: \
`python3 post-processing/fundamental_diagram.py` \
and \
`python3 post-processing/width_comparison.py`

## Fundamental Diagram Comparison to Experimental Data
To generate graphics, run: \
`python3 post-processing/fundamental_diagram_comparison.py`

## Fundamental Diagram Error Minimization
To generate graphics, run: \
`python3 post-processing/error_minimization.py`