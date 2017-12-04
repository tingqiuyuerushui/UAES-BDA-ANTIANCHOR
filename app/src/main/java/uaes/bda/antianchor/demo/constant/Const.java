package uaes.bda.antianchor.demo.constant;

public class Const { //http://139.224.8.68:6789/v1/gas/station/service/historyByMile
	public static String Domain = "http://139.224.8.68:6789";
	public static String TestDomain = "http://192.168.43.110:12345";


	/*http://139.224.8.68:6789/v1/sparking/status
	* 火花塞数据接口
	* */
	public static final String UrlPostSparking = Domain + "/v1/sparking/status";
	/*
	* 获取用油助手数据
	*/
	public static final String UrlGetOilHelper = Domain + "/v1/gas/vehicle";
	/*
	* 获取对应汽油标号单位油价
	*/
	public static final String UrlGetOilPrice = Domain + "/v1/gas/price";
	/*
	* 提交记账数据
	*/
	public static final String UrlPostNoteData = Domain + "/v1/gas/station/service/record";
	/*
	* 设置低油量预警值
	*/
	public static final String UrlPostLowOilWarningData = Domain + "/v1/gas/service/warning";
	/*
	* 根据日期查询用油历史
	*/
	//public static final String UrlPostFuelHistoryByDate = TestDomain + "/v1/gas/station/service/history";
											//http://139.224.8.68:6789/v1/gas/station/service/historyByDate
	public static final String UrlPostFuelHistoryByDate = Domain + "/v1/gas/station/service/historyByDate";


    /*根据里程查询用油历史*/
    public static final String UrlPostFuelHistoryByMile = Domain + "/v1/gas/station/service/historyByMile";
	/*
	* 获取单次加油记录列表
	*/
	public static final String UrlPostFuelRecordList = Domain + "/v1/gas/station/service/list";
	/*
	* 获取电池状态websocket
	*/
	public static final String UrlWebSocketGetBatteryStatus = "ws://139.224.8.68:5679/batteryStatus";
	/*
	* 获取机油状态websocket
	*/
	public static final String UrlWebSocketGetOilStatus = "ws://139.224.8.68:5681/engineoilStatus";
	/*
	* 获取火花塞状态websocket
	*/
	public static final String UrlWebSocketGetSparkingStatus = "ws://139.224.8.68:5680/sparkingStatus";
	/*
	* 获取电池状态
	*/
	public static final String UrlBatteryStatus = Domain + "/v1/battery/status";
	/*
	* 获取机油状态
	*/
	public static final String UrlEngineOilStatus = Domain + "/v1/engineOil/status";
	/*
	* 获取火花塞状态
	*/
	public static final String UrlSparkingStatus = Domain +  "/v1/sparking/status";
	/*
	* 获取首页状态
	*/
	public static final String UrlHomeStatus = Domain +  "/v1/mainPage";
	/*
	* app更新接口
	*/
	public static final String UrlGetVersion = Domain + "/Login/UpdateOfApp";
	/*
	* 登录接口
	*/
	public static String UrlLogin = Domain + "/Login/Login";
	public static String VersionUrl = "";
	public static int VersionNum = 0;
	public static final int NOTIFICATION = 15389;
	public static String SDRootPath = "./uaes";
	public static String city = "上海";
	public static String district = "浦东新区";
	//维度
	public static String Latitude = "121.6342295944";
	//经度
	public static String Longitude = "31.2730723817";
	/*
	* 获取心知未来几天内的天气
	*/
	public static final String UrlXinZhiWeatherDay = "https://api.seniverse.com/v3/weather/daily.json?key=hnjzj0m5vgv3ee0q&";
	/*
	* 获取心知实时天气信息
	*/
	public static final String UrlXinZhiWeatherRealTime = "https://api.seniverse.com/v3/weather/now.json?key=hnjzj0m5vgv3ee0q&";
	/*
	* 获取心知空气质量
	*/
	public static final String UrlXinZhiAirQuilty = "https://api.seniverse.com/v3/air/now.json?key=hnjzj0m5vgv3ee0q&";
}
