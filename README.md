# UserRequest Aggregate Data Consolidator

### Instructions on Running

###### Run App
* java -jar runnable.jar "yyyy/MM/dd"

###### Test App
* java -jar runtests.jar

### Purpose

1. Takes aggregate grouping of partitions and condenses them.
    1. Inputs csv format (datetime, userid, platformid, requestcount)
    2. Outputs csv format (userid, platformid, requestcount)
1. Outputs are a consolidation of all unique events by a (user) on a (platform)

###### Functionality

1. CSV => 2020/08/12/test.csv & 2020/08/10/test.csv
    1. 12/08/2020 16:00,1,2,498
    1. 12/08/2020 16:00,1,2,688
    1. 12/10/2020 16:00,1,3,597
    1. 12/10/2020 16:00,1,3,86
1. Become Two Files
    1. 2020/08/12/output.csv ===> 1,2,1186
    1. 2020/19/12/output.csv ===> 1,3,683
    
### Thought Process

1. Efficiency
    1. Matching Existing records is done O(n), assumption being each csv is to large to implement matching with a hash

###### Testing

1. Tests are snapshot based, and by no means perfect at "locking down" the code
2. To implement further, you would want to pretty much emulate every action in the
workflow to get 100% coverage