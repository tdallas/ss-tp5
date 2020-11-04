# ss-tp4
# Engine and simulation
## Generate executable
`mvn clean install`

## Execute command to generate all simulations

`java -jar target/ss-tp4-1.0.jar`

## Output
The program will generate all the files with similar formats like these:

`out/planets-FILENAME.xyz`

`out/oscillators-FILENAME.csv`

# Post processing

**In every script, the first lines contains the file that is about to beein processed, if you want to graphic some custom behaviour, just modified the file path to match your custom file**

## Oscilator
To show oscilators graphics, run: \
`python3 post-processing/oscilator.py` \
and \
`python3 post-processing/oscilator_errors.py`

## Planets
To show planets orbit graphics, run: \
`python3 post-processing/planets-distance-1-year-big-delta.py` \
and \
`python3 post-processing/planets-distance-1-year-small-delta.py`

## Mars
To show Mars graphics, run: \
`python3 post-processing/mars-velocity-best-day.py` \
and \
`python3 post-processing/mars-min-distance-per-minute.py` \
and \
`python3 post-processing/mars-min-distance-per-hour.py` \
and \
`python3 post-processing/mars-min-distance-per-day.py` 

## Jupiter
To show Jupiter graphics, run: \
`python3 post-processing/jupiter-velocity-best-day.py` \
and \
`python3 post-processing/jupiter-min-distance-per-minute.py` \
and \
`python3 post-processing/jupiter-min-distance-per-hour.py` \
and \
`python3 post-processing/jupiter-min-distance-per-day.py` 