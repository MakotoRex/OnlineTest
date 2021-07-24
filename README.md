# README

## App Description

1. We firstly try the private server API with timeout 3 seconds. If success, show "Private Api : {"status":200,"message":"OK"}"on the top of page, else we try the public server API. If success, show "Public Api : {"status":200,"message":"OK"}"on the top of page, else we show onFailure message.

2. When the public or private API success, start showing the PassWallet Info with two group(Day Pass & Hour Pass). User can buy different types of Pass(7Days, 3Days, 1Day, 8Hours & 4Hours) at any time they want. But there is only one activating Pass. Before buying it, PassInfo only shows the price. After Buying it, PassInfo shows the purchased time. When the pass is activated, PassInfo shows the activation time & expiration time.


### Question 1 Explanation

There is no info about private server WIFI(ssid & password), so assume that user do the WIFI connection before starting MainActivity.

1. Before Android Q, assume that the private WiFi is connected,
simply do "bindProcessToNetwork" with "NetworkCapabilities.TRANSPORT_WIFI"
or with "NetworkCapabilities.TRANSPORT_CELLULAR" if you want to send API by WIFI or mobile network. You can switch binding at any time you want.

2. For Android Q and the newer version, if you want to send APIs by limited WIFI, you have to connect to WIFI in App by using "NetworkRequest" and then do "bindProcessToNetwork" in NetworkCallback.onAvailable when WIFI is connected.

See details in NetworkUtils.java

### Question 2 Explanation

PassWallet works as the description on the top.

1. There is a Handler checking & updating the expiration once per second (start/stop with  onResume/onPause). The expired pass will be changed to the state before purchased, and then you can activate another pass.
For verifying this, you can simply change the pass duration in DateTimeUtils.java like this:
c.add(Calendar.HOUR, 4);  -> c.add(Calendar.SECOND, 4);


### ScreenShot

![image](https://github.com/MakotoRex/OnlineTest/blob/master/connecting.jpg?raw=false)

![image](https://github.com/MakotoRex/OnlineTest/blob/master/public_server.jpg?raw=false)

![image](https://github.com/MakotoRex/OnlineTest/blob/master/wallet.jpg?raw=true)
