import os, sys
from datetime import datetime
from DataStore import *
from ReadData import *
from JobManager import *
from Network import *
#from Generate_Grid import *

def get_immediate_subdirectories(dir):
    return [name for name in os.listdir(dir) if os.path.isdir(os.path.join(dir, name))]

sys.path += get_immediate_subdirectories("./")

from ReadConfig import *
from cmonkey import *
from Helpers import *

# Instantsiate settings file
settings = {}
settings = ReadConfig(settings)
settings["global"]["working_dir"] = os.getcwd() + '/'
settings["global"]["experiment_name"] = "MCZ-DFG4grn-"+sys.argv[1]



# Create date string to append to output_dir
t = datetime.now().strftime("%Y-%m-%d_%H.%M.%S")
settings["global"]["output_dir"] = settings["global"]["output_dir"] + "/" + \
    settings["global"]["experiment_name"] + "-" + t + "/"
os.mkdir(settings["global"]["output_dir"])

# Read in the gold standard network

# Read in the gold standard network
goldnet = Network()
#goldnet.read_goldstd(settings["global"]["large_network_goldnet_file"])
ko_file, kd_file, ts_file, wt_file, mf_file, goldnet = get_example_data_files(sys.argv[1], settings)


# Read data into program
# Where the format is "FILENAME" "DATATYPE"
knockout_storage = ReadData(ko_file[0], "knockout")
knockdown_storage = ReadData(kd_file[0], "knockdown")
timeseries_storage = ReadData(ts_file[0], "timeseries")
wildtype_storage = ReadData(wt_file[0], "wildtype")



# Setup job manager
jobman = JobManager(settings)

clusterjob = Cmonkey()
clusterjob.setup(knockout_storage, settings)

jobman.queueJob(clusterjob)
jobman.runQueue()
jobman.waitToClear()

