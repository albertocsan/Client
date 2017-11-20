package cache.client

import java.util.HashMap

class Workbench(kinesisStream: String) {
  val devices : HashMap[Long, Device] = new HashMap[Long, Device]
  def execute(data_blob_count : Int, device_id : Long){    
    
    var device : Device = devices.get(device_id);

    if (device != null){
      device = devices.get(device_id)
    } else {
      device = new Device(kinesisStream)
      devices.put(device_id, device)
    }
    device.execute()
  }
  
  }