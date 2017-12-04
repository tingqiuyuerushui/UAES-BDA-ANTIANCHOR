package uaes.bda.antianchor.demo.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Petrol implements Parcelable {
	
	private String type;//型号
	private String price;//油价
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	@Override
	public int describeContents() {
		//返回0
		return 0;
	}
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// 序列化过程：必须按照成员变量声明的顺序进行封装
		arg0.writeString(type);
		arg0.writeString(price);
	}

	//反序列化过程：必须实现parcelable。Creator接口，并且对象名必须为CREATOR
	//读取parecel里面数据时必须按照成员变量声明的顺序，parecel数据来源上面writeToParecel方法
	//出来的数据供逻辑层使用
	public static final Creator<Petrol> CREATOR = new Creator<Petrol>() {

		@Override
		public Petrol createFromParcel(Parcel arg0) {
			// TODO Auto-generated method stub
			Petrol p = new Petrol();
			p.type = arg0.readString();
			p.price = arg0.readString();
			return p;
		}

		@Override
		public Petrol[] newArray(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}
	};
	
}
