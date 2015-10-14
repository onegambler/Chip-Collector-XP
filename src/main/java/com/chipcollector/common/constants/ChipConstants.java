package com.chipcollector.common.constants;

public class ChipConstants {
	
	public enum ChipDetails{
		DENOM("denom"),
		ISSUED("issued"),
		COLOR("color"),
		INSERTS("inserts"),
		MOLD("mold"),
		TCR("tcr");

		private String value;
		
		ChipDetails(String value) {
			this.value = value;
		}
		
		public String getValue(){
			return value;
		}
	}

}
