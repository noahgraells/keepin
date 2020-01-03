package ch.hesso.keepin.pojos

import android.hardware.usb.UsbEndpoint
import ch.hesso.keepin.enums.Status

data class PublicUser(val endpointId : String,
                      val name: String,
                      var status: Status,
                      val lastName: String = "")