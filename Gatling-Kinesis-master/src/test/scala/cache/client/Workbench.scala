package cache.client

import java.util.HashMap

class Workbench(kinesisStream: String, sessionType: String, listActions: List[String], region: String) {
  val devices : HashMap[Long, Device] = new HashMap[Long, Device]
  def execute(device_id : Long){
    var device : Device = devices.get(device_id);
    if (device != null){
      device = devices.get(device_id)
    } else {
      device = new Device(kinesisStream, sessionType, listActions, region)
      devices.put(device_id, device)
    }
    device.execute()
  }
  
  }