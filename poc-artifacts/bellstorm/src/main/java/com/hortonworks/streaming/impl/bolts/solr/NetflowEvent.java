package com.hortonworks.streaming.impl.bolts.solr;

import java.util.Date;
import org.apache.solr.client.solrj.beans.Field;

public class NetflowEvent {
	
	@Field
	String id;
	@Field
	Date nf_Start ;
	@Field
	Date nf_End ;
	@Field
	Float nf_Duration ;
	@Field
	String nf_SrcAddr ;
	@Field
	String nf_DstAddr ;
	@Field
	Long nf_SrdPort ;
	@Field
	Long nf_DstPort ;
	@Field
	String nf_Proto ;
	@Field
	String nf_flg ;
	@Field
	Long nf_fwd ;
	@Field
	Long nf_stos ;
	@Field
	Long nf_ipkt ;
	@Field
	String nf_ibyt ;
	@Field
	String nf_opkt ;
	@Field
	Long nf_obyt ;
	@Field
	Long nf_inIF ;
	@Field
	Long nf_outIF ;
	@Field
	Long nf_sAS ;
	@Field
	Long nf_dAS ;
	@Field
	Long nf_smk ;
	@Field
	Long nf_dmk ;
	@Field
	Long nf_dtos ;
	@Field
	Long nf_dir ;
	@Field
	String nf_NextHop ;
	@Field
	String nf_BGPNextHop ;
	@Field
	Long nf_svln ;
	@Field
	Long nf_dvln ;
	@Field
	String nf_ismc ;
	@Field
	String nf_odmc ;
	@Field
	String nf_idmc ;
	@Field
	String nf_osmc ;
	@Field
	String nf_mpls1 ;
	@Field
	String nf_mpls2 ;
	@Field
	String nf_mpls3 ;
	@Field
	String nf_mpls4 ;
	@Field
	String nf_mpls5 ;
	@Field
	String nf_mpls6 ;
	@Field
	String nf_mpls7 ;
	@Field
	String nf_mpls8 ;
	@Field
	String nf_mpls9 ;
	@Field
	String nf_mpls10 ;
	@Field
	Float nf_cl ;
	@Field
	Float nf_sl ;
	@Field
	Float nf_al ;
	@Field
	String nf_RouterIP ;
	@Field
	String nf_eng ;
	@Field
	Long nf_exid ;
	@Field
	String nf_tr ;
	@Field
	String source_ip_data_Source ;
	@Field
	String source_ip_data_Name ;
	@Field
	String source_ip_data_Device ;
	@Field
	String source_ip_data_Interface ;
	@Field
	String source_ip_data_Continent ;
	@Field
	String source_ip_data_Country ;
	@Field
	String source_ip_data_State ;
	@Field
	String source_ip_data_City ;
	@Field
	Float  source_ip_data_Lat ;
	@Field
	Float  source_ip_data_Long ;
	@Field
	Float  source_ip_data_Timestamp ;
	@Field
	String dest_ip_data_Source ;
	@Field
	String dest_ip_data_Name ;
	@Field
	String dest_ip_data_Device ;
	@Field
	String dest_ip_data_Interface ;
	@Field
	String dest_ip_data_Continent ;
	@Field
	String dest_ip_data_Country ;
	@Field
	String dest_ip_data_State ;
	@Field
	String dest_ip_data_City ;
	@Field
	Float  dest_ip_data_Lat ;
	@Field
	Float  dest_ip_data_Long ;
	@Field
	Float  dest_ip_data_Timestamp ;
	@Field
	String source_geo;
	@Field 
	String dest_geo;

	public NetflowEvent(String id, Date nf_Start ,Date nf_End ,Float nf_Duration ,String nf_SrcAddr ,String nf_DstAddr 
		,Long nf_SrdPort ,Long nf_DstPort ,String nf_Proto ,String nf_flg ,Long nf_fwd ,Long nf_stos ,Long nf_ipkt 
		,String nf_ibyt ,String nf_opkt ,Long nf_obyt ,Long nf_inIF ,Long nf_outIF ,Long nf_sAS ,Long nf_dAS ,Long nf_smk 
		,Long nf_dmk ,Long nf_dtos ,Long nf_dir ,String nf_NextHop ,String nf_BGPNextHop ,Long nf_svln ,Long nf_dvln 
		,String nf_ismc ,String nf_odmc ,String nf_idmc ,String nf_osmc ,String nf_mpls1 ,String nf_mpls2 
		,String nf_mpls3 ,String nf_mpls4 ,String nf_mpls5 ,String nf_mpls6 ,String nf_mpls7 ,String nf_mpls8 
		,String nf_mpls9 ,String nf_mpls10 ,Float nf_cl ,Float nf_sl ,Float nf_al ,String nf_RouterIP ,String nf_eng 
		,Long nf_exid ,String nf_tr, String source_ip_data_Source ,String source_ip_data_Name ,String source_ip_data_Device ,
		String source_ip_data_Interface ,String source_ip_data_Continent ,String source_ip_data_Country ,String source_ip_data_State ,
		String source_ip_data_City ,Float  source_ip_data_Lat ,Float  source_ip_data_Long ,Float  source_ip_data_Timestamp ,
		String dest_ip_data_Source ,String dest_ip_data_Name ,String dest_ip_data_Device ,String dest_ip_data_Interface ,
		String dest_ip_data_Continent ,String dest_ip_data_Country ,String dest_ip_data_State ,String dest_ip_data_City ,
		Float  dest_ip_data_Lat ,Float  dest_ip_data_Long ,Float  dest_ip_data_Timestamp, String src_geo, String dest_geo ) {
		super();
		this.id=id;
		this.nf_Start=nf_Start;
		this.nf_End=nf_End;
		this.nf_Duration=nf_Duration;
		this.nf_SrcAddr=nf_SrcAddr;
		this.nf_DstAddr=nf_DstAddr;
		this.nf_SrdPort=nf_SrdPort;
		this.nf_DstPort=nf_DstPort;
		this.nf_Proto=nf_Proto;
		this.nf_flg=nf_flg;
		this.nf_fwd=nf_fwd;
		this.nf_stos=nf_stos;
		this.nf_ipkt=nf_ipkt;
		this.nf_ibyt=nf_ibyt;
		this.nf_opkt=nf_opkt;
		this.nf_obyt=nf_obyt;
		this.nf_inIF=nf_inIF;
		this.nf_outIF=nf_outIF;
		this.nf_sAS=nf_sAS;
		this.nf_dAS=nf_dAS;
		this.nf_smk=nf_smk;
		this.nf_dmk=nf_dmk;
		this.nf_dtos=nf_dtos;
		this.nf_dir=nf_dir;
		this.nf_NextHop=nf_NextHop;
		this.nf_BGPNextHop=nf_BGPNextHop;
		this.nf_svln=nf_svln;
		this.nf_dvln=nf_dvln;
		this.nf_ismc=nf_ismc;
		this.nf_odmc=nf_odmc;
		this.nf_idmc=nf_idmc;
		this.nf_osmc=nf_osmc;
		this.nf_mpls1=nf_mpls1;
		this.nf_mpls2=nf_mpls2;
		this.nf_mpls3=nf_mpls3;
		this.nf_mpls4=nf_mpls4;
		this.nf_mpls5=nf_mpls5;
		this.nf_mpls6=nf_mpls6;
		this.nf_mpls7=nf_mpls7;
		this.nf_mpls8=nf_mpls8;
		this.nf_mpls9=nf_mpls9;
		this.nf_mpls10=nf_mpls10;
		this.nf_cl=nf_cl;
		this.nf_sl=nf_sl;
		this.nf_al=nf_al;
		this.nf_RouterIP=nf_RouterIP;
		this.nf_eng=nf_eng;
		this.nf_exid=nf_exid;
		this.nf_tr=nf_tr;
		this.source_ip_data_Source=source_ip_data_Source;
		this.source_ip_data_Name=source_ip_data_Name;
		this.source_ip_data_Device=source_ip_data_Device;
		this.source_ip_data_Interface=source_ip_data_Interface;
		this.source_ip_data_Continent=source_ip_data_Continent;
		this.source_ip_data_Country=source_ip_data_Country;
		this.source_ip_data_State=source_ip_data_State;
		this.source_ip_data_City=source_ip_data_City;
		this.source_ip_data_Lat=source_ip_data_Lat;
		this.source_ip_data_Long=source_ip_data_Long;
		this.source_ip_data_Timestamp=source_ip_data_Timestamp;
		this.dest_ip_data_Source=dest_ip_data_Source;
		this.dest_ip_data_Name=dest_ip_data_Name;
		this.dest_ip_data_Device=dest_ip_data_Device;
		this.dest_ip_data_Interface=dest_ip_data_Interface;
		this.dest_ip_data_Continent=dest_ip_data_Continent;
		this.dest_ip_data_Country=dest_ip_data_Country;
		this.dest_ip_data_State=dest_ip_data_State;
		this.dest_ip_data_City=dest_ip_data_City;
		this.dest_ip_data_Lat=dest_ip_data_Lat;
		this.dest_ip_data_Long=dest_ip_data_Long;
		this.dest_ip_data_Timestamp=dest_ip_data_Timestamp;
		this.source_geo=source_geo;
		this.dest_geo=dest_geo;		
	}

	public String get_id() { return id; }
	public Date getnf_Start() { return nf_Start; }
	public Date getnf_End() { return nf_End; }
	public Float getnf_Duration() { return nf_Duration; }
	public String getnf_SrcAddr() { return nf_SrcAddr; }
	public String getnf_DstAddr() { return nf_DstAddr; }
	public Long getnf_SrdPort() { return nf_SrdPort; }
	public Long getnf_DstPort() { return nf_DstPort; }
	public String getnf_Proto() { return nf_Proto; }
	public String getnf_flg() { return nf_flg; }
	public Long getnf_fwd() { return nf_fwd; }
	public Long getnf_stos() { return nf_stos; }
	public Long getnf_ipkt() { return nf_ipkt; }
	public String getnf_ibyt() { return nf_ibyt; }
	public String getnf_opkt() { return nf_opkt; }
	public Long getnf_obyt() { return nf_obyt; }
	public Long getnf_inIF() { return nf_inIF; }
	public Long getnf_outIF() { return nf_outIF; }
	public Long getnf_sAS() { return nf_sAS; }
	public Long getnf_dAS() { return nf_dAS; }
	public Long getnf_smk() { return nf_smk; }
	public Long getnf_dmk() { return nf_dmk; }
	public Long getnf_dtos() { return nf_dtos; }
	public Long getnf_dir() { return nf_dir; }
	public String getnf_NextHop() { return nf_NextHop; }
	public String getnf_BGPNextHop() { return nf_BGPNextHop; }
	public Long getnf_svln() { return nf_svln; }
	public Long getnf_dvln() { return nf_dvln; }
	public String getnf_ismc() { return nf_ismc; }
	public String getnf_odmc() { return nf_odmc; }
	public String getnf_idmc() { return nf_idmc; }
	public String getnf_osmc() { return nf_osmc; }
	public String getnf_mpls1() { return nf_mpls1; }
	public String getnf_mpls2() { return nf_mpls2; }
	public String getnf_mpls3() { return nf_mpls3; }
	public String getnf_mpls4() { return nf_mpls4; }
	public String getnf_mpls5() { return nf_mpls5; }
	public String getnf_mpls6() { return nf_mpls6; }
	public String getnf_mpls7() { return nf_mpls7; }
	public String getnf_mpls8() { return nf_mpls8; }
	public String getnf_mpls9() { return nf_mpls9; }
	public String getnf_mpls10() { return nf_mpls10; }
	public Float getnf_cl() { return nf_cl; }
	public Float getnf_sl() { return nf_sl; }
	public Float getnf_al() { return nf_al; }
	public String getnf_RouterIP() { return nf_RouterIP; }
	public String getnf_eng() { return nf_eng; }
	public Long getnf_exid() { return nf_exid; }
	public String getnf_tr() { return nf_tr; }
	public String getsource_ip_data_Source() { return source_ip_data_Source; }
	public String getsource_ip_data_Name() { return source_ip_data_Name; }
	public String getsource_ip_data_Device() { return source_ip_data_Device; }
	public String getsource_ip_data_Interface() { return source_ip_data_Interface; }
	public String getsource_ip_data_Continent() { return source_ip_data_Continent; }
	public String getsource_ip_data_Country() { return source_ip_data_Country; }
	public String getsource_ip_data_State() { return source_ip_data_State; }
	public String getsource_ip_data_City() { return source_ip_data_City; }
	public Float getsource_ip_data_Lat() { return source_ip_data_Lat; }
	public Float getsource_ip_data_Long() { return source_ip_data_Long; }
	public Float getsource_ip_data_Timestamp() { return source_ip_data_Timestamp; }
	public String getdest_ip_data_Source() { return dest_ip_data_Source; }
	public String getdest_ip_data_Name() { return dest_ip_data_Name; }
	public String getdest_ip_data_Device() { return dest_ip_data_Device; }
	public String getdest_ip_data_Interface() { return dest_ip_data_Interface; }
	public String getdest_ip_data_Continent() { return dest_ip_data_Continent; }
	public String getdest_ip_data_Country() { return dest_ip_data_Country; }
	public String getdest_ip_data_State() { return dest_ip_data_State; }
	public String getdest_ip_data_City() { return dest_ip_data_City; }
	public Float getdest_ip_data_Lat() { return dest_ip_data_Lat; }
	public Float getdest_ip_data_Long() { return dest_ip_data_Long; }
	public Float getdest_ip_data_Timestamp() { return dest_ip_data_Timestamp; }
	public String getsource_geo() {return source_geo; }
	public String getdest_geo() {return dest_geo; }

	public void set_id(String id) { this.id = id; }
	public void setnf_Start(Date nf_Start) { this.nf_Start = nf_Start; }
	public void setnf_End(Date nf_End) { this.nf_End = nf_End; }
	public void setnf_Duration(Float nf_Duration) { this.nf_Duration = nf_Duration; }
	public void setnf_SrcAddr(String nf_SrcAddr) { this.nf_SrcAddr = nf_SrcAddr; }
	public void setnf_DstAddr(String nf_DstAddr) { this.nf_DstAddr = nf_DstAddr; }
	public void setnf_SrdPort(Long nf_SrdPort) { this.nf_SrdPort = nf_SrdPort; }
	public void setnf_DstPort(Long nf_DstPort) { this.nf_DstPort = nf_DstPort; }
	public void setnf_Proto(String nf_Proto) { this.nf_Proto = nf_Proto; }
	public void setnf_flg(String nf_flg) { this.nf_flg = nf_flg; }
	public void setnf_fwd(Long nf_fwd) { this.nf_fwd = nf_fwd; }
	public void setnf_stos(Long nf_stos) { this.nf_stos = nf_stos; }
	public void setnf_ipkt(Long nf_ipkt) { this.nf_ipkt = nf_ipkt; }
	public void setnf_ibyt(String nf_ibyt) { this.nf_ibyt = nf_ibyt; }
	public void setnf_opkt(String nf_opkt) { this.nf_opkt = nf_opkt; }
	public void setnf_obyt(Long nf_obyt) { this.nf_obyt = nf_obyt; }
	public void setnf_inIF(Long nf_inIF) { this.nf_inIF = nf_inIF; }
	public void setnf_outIF(Long nf_outIF) { this.nf_outIF = nf_outIF; }
	public void setnf_sAS(Long nf_sAS) { this.nf_sAS = nf_sAS; }
	public void setnf_dAS(Long nf_dAS) { this.nf_dAS = nf_dAS; }
	public void setnf_smk(Long nf_smk) { this.nf_smk = nf_smk; }
	public void setnf_dmk(Long nf_dmk) { this.nf_dmk = nf_dmk; }
	public void setnf_dtos(Long nf_dtos) { this.nf_dtos = nf_dtos; }
	public void setnf_dir(Long nf_dir) { this.nf_dir = nf_dir; }
	public void setnf_NextHop(String nf_NextHop) { this.nf_NextHop = nf_NextHop; }
	public void setnf_BGPNextHop(String nf_BGPNextHop) { this.nf_BGPNextHop = nf_BGPNextHop; }
	public void setnf_svln(Long nf_svln) { this.nf_svln = nf_svln; }
	public void setnf_dvln(Long nf_dvln) { this.nf_dvln = nf_dvln; }
	public void setnf_ismc(String nf_ismc) { this.nf_ismc = nf_ismc; }
	public void setnf_odmc(String nf_odmc) { this.nf_odmc = nf_odmc; }
	public void setnf_idmc(String nf_idmc) { this.nf_idmc = nf_idmc; }
	public void setnf_osmc(String nf_osmc) { this.nf_osmc = nf_osmc; }
	public void setnf_mpls1(String nf_mpls1) { this.nf_mpls1 = nf_mpls1; }
	public void setnf_mpls2(String nf_mpls2) { this.nf_mpls2 = nf_mpls2; }
	public void setnf_mpls3(String nf_mpls3) { this.nf_mpls3 = nf_mpls3; }
	public void setnf_mpls4(String nf_mpls4) { this.nf_mpls4 = nf_mpls4; }
	public void setnf_mpls5(String nf_mpls5) { this.nf_mpls5 = nf_mpls5; }
	public void setnf_mpls6(String nf_mpls6) { this.nf_mpls6 = nf_mpls6; }
	public void setnf_mpls7(String nf_mpls7) { this.nf_mpls7 = nf_mpls7; }
	public void setnf_mpls8(String nf_mpls8) { this.nf_mpls8 = nf_mpls8; }
	public void setnf_mpls9(String nf_mpls9) { this.nf_mpls9 = nf_mpls9; }
	public void setnf_mpls10(String nf_mpls10) { this.nf_mpls10 = nf_mpls10; }
	public void setnf_cl(Float nf_cl) { this.nf_cl = nf_cl; }
	public void setnf_sl(Float nf_sl) { this.nf_sl = nf_sl; }
	public void setnf_al(Float nf_al) { this.nf_al = nf_al; }
	public void setnf_RouterIP(String nf_RouterIP) { this.nf_RouterIP = nf_RouterIP; }
	public void setnf_eng(String nf_eng) { this.nf_eng = nf_eng; }
	public void setnf_exid(Long nf_exid) { this.nf_exid = nf_exid; }
	public void setnf_tr(String nf_tr) { this.nf_tr = nf_tr; }
	public void setsource_ip_data_Source(String source_ip_data_Source) { this.source_ip_data_Source = source_ip_data_Source; }
	public void setsource_ip_data_Name(String source_ip_data_Name) { this.source_ip_data_Name = source_ip_data_Name; }
	public void setsource_ip_data_Device(String source_ip_data_Device) { this.source_ip_data_Device = source_ip_data_Device; }
	public void setsource_ip_data_Interface(String source_ip_data_Interface) { this.source_ip_data_Interface = source_ip_data_Interface; }
	public void setsource_ip_data_Continent(String source_ip_data_Continent) { this.source_ip_data_Continent = source_ip_data_Continent; }
	public void setsource_ip_data_Country(String source_ip_data_Country) { this.source_ip_data_Country = source_ip_data_Country; }
	public void setsource_ip_data_State(String source_ip_data_State) { this.source_ip_data_State = source_ip_data_State; }
	public void setsource_ip_data_City(String source_ip_data_City) { this.source_ip_data_City = source_ip_data_City; }
	public void setsource_ip_data_Lat(Float source_ip_data_Lat) { this.source_ip_data_Lat = source_ip_data_Lat; }
	public void setsource_ip_data_Long(Float source_ip_data_Long) { this.source_ip_data_Long = source_ip_data_Long; }
	public void setsource_ip_data_Timestamp(Float source_ip_data_Timestamp) { this.source_ip_data_Timestamp = source_ip_data_Timestamp; }
	public void setdest_ip_data_Source(String dest_ip_data_Source) { this.dest_ip_data_Source = dest_ip_data_Source; }
	public void setdest_ip_data_Name(String dest_ip_data_Name) { this.dest_ip_data_Name = dest_ip_data_Name; }
	public void setdest_ip_data_Device(String dest_ip_data_Device) { this.dest_ip_data_Device = dest_ip_data_Device; }
	public void setdest_ip_data_Interface(String dest_ip_data_Interface) { this.dest_ip_data_Interface = dest_ip_data_Interface; }
	public void setdest_ip_data_Continent(String dest_ip_data_Continent) { this.dest_ip_data_Continent = dest_ip_data_Continent; }
	public void setdest_ip_data_Country(String dest_ip_data_Country) { this.dest_ip_data_Country = dest_ip_data_Country; }
	public void setdest_ip_data_State(String dest_ip_data_State) { this.dest_ip_data_State = dest_ip_data_State; }
	public void setdest_ip_data_City(String dest_ip_data_City) { this.dest_ip_data_City = dest_ip_data_City; }
	public void setdest_ip_data_Lat(Float dest_ip_data_Lat) { this.dest_ip_data_Lat = dest_ip_data_Lat; }
	public void setdest_ip_data_Long(Float dest_ip_data_Long) { this.dest_ip_data_Long = dest_ip_data_Long; }
	public void setdest_ip_data_Timestamp(Float dest_ip_data_Timestamp) { this.dest_ip_data_Timestamp = dest_ip_data_Timestamp; }
	public void setdest_geo(String dest_geo) { this.dest_geo = dest_geo; }
	public void setsource_geo(String source_geo) { this.source_geo = source_geo; }

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("id:").append(id).append(", ");
		buffer.append("nf_Start:").append(nf_Start).append(", ");
		buffer.append("nf_End:").append(nf_End).append(", ");
		buffer.append("nf_Duration:").append(nf_Duration).append(", ");
		buffer.append("nf_SrcAddr:").append(nf_SrcAddr).append(", ");
		buffer.append("nf_DstAddr:").append(nf_DstAddr).append(", ");
		buffer.append("nf_SrdPort:").append(nf_SrdPort).append(", ");
		buffer.append("nf_DstPort:").append(nf_DstPort).append(", ");
		buffer.append("nf_Proto:").append(nf_Proto).append(", ");
		buffer.append("nf_flg:").append(nf_flg).append(", ");
		buffer.append("nf_fwd:").append(nf_fwd).append(", ");
		buffer.append("nf_stos:").append(nf_stos).append(", ");
		buffer.append("nf_ipkt:").append(nf_ipkt).append(", ");
		buffer.append("nf_ibyt:").append(nf_ibyt).append(", ");
		buffer.append("nf_opkt:").append(nf_opkt).append(", ");
		buffer.append("nf_obyt:").append(nf_obyt).append(", ");
		buffer.append("nf_inIF:").append(nf_inIF).append(", ");
		buffer.append("nf_outIF:").append(nf_outIF).append(", ");
		buffer.append("nf_sAS:").append(nf_sAS).append(", ");
		buffer.append("nf_dAS:").append(nf_dAS).append(", ");
		buffer.append("nf_smk:").append(nf_smk).append(", ");
		buffer.append("nf_dmk:").append(nf_dmk).append(", ");
		buffer.append("nf_dtos:").append(nf_dtos).append(", ");
		buffer.append("nf_dir:").append(nf_dir).append(", ");
		buffer.append("nf_NextHop:").append(nf_NextHop).append(", ");
		buffer.append("nf_BGPNextHop:").append(nf_BGPNextHop).append(", ");
		buffer.append("nf_svln:").append(nf_svln).append(", ");
		buffer.append("nf_dvln:").append(nf_dvln).append(", ");
		buffer.append("nf_ismc:").append(nf_ismc).append(", ");
		buffer.append("nf_odmc:").append(nf_odmc).append(", ");
		buffer.append("nf_idmc:").append(nf_idmc).append(", ");
		buffer.append("nf_osmc:").append(nf_osmc).append(", ");
		buffer.append("nf_mpls1:").append(nf_mpls1).append(", ");
		buffer.append("nf_mpls2:").append(nf_mpls2).append(", ");
		buffer.append("nf_mpls3:").append(nf_mpls3).append(", ");
		buffer.append("nf_mpls4:").append(nf_mpls4).append(", ");
		buffer.append("nf_mpls5:").append(nf_mpls5).append(", ");
		buffer.append("nf_mpls6:").append(nf_mpls6).append(", ");
		buffer.append("nf_mpls7:").append(nf_mpls7).append(", ");
		buffer.append("nf_mpls8:").append(nf_mpls8).append(", ");
		buffer.append("nf_mpls9:").append(nf_mpls9).append(", ");
		buffer.append("nf_mpls10:").append(nf_mpls10).append(", ");
		buffer.append("nf_cl:").append(nf_cl).append(", ");
		buffer.append("nf_sl:").append(nf_sl).append(", ");
		buffer.append("nf_al:").append(nf_al).append(", ");
		buffer.append("nf_RouterIP:").append(nf_RouterIP).append(", ");
		buffer.append("nf_eng:").append(nf_eng).append(", ");
		buffer.append("nf_exid:").append(nf_exid).append(", ");
		buffer.append("nf_tr:").append(nf_tr).append(", ");
		buffer.append("source_ip_data_Source:").append(source_ip_data_Source).append(", ");
		buffer.append("source_ip_data_Name:").append(source_ip_data_Name).append(", ");
		buffer.append("source_ip_data_Device:").append(source_ip_data_Device).append(", ");
		buffer.append("source_ip_data_Interface:").append(source_ip_data_Interface).append(", ");
		buffer.append("source_ip_data_Continent:").append(source_ip_data_Continent).append(", ");
		buffer.append("source_ip_data_Country:").append(source_ip_data_Country).append(", ");
		buffer.append("source_ip_data_State:").append(source_ip_data_State).append(", ");
		buffer.append("source_ip_data_City:").append(source_ip_data_City).append(", ");
		buffer.append("source_ip_data_Lat:").append(source_ip_data_Lat).append(", ");
		buffer.append("source_ip_data_Long:").append(source_ip_data_Long).append(", ");
		buffer.append("source_ip_data_Timestamp:").append(source_ip_data_Timestamp).append(", ");
		buffer.append("dest_ip_data_Source:").append(dest_ip_data_Source).append(", ");
		buffer.append("dest_ip_data_Name:").append(dest_ip_data_Name).append(", ");
		buffer.append("dest_ip_data_Device:").append(dest_ip_data_Device).append(", ");
		buffer.append("dest_ip_data_Interface:").append(dest_ip_data_Interface).append(", ");
		buffer.append("dest_ip_data_Continent:").append(dest_ip_data_Continent).append(", ");
		buffer.append("dest_ip_data_Country:").append(dest_ip_data_Country).append(", ");
		buffer.append("dest_ip_data_State:").append(dest_ip_data_State).append(", ");
		buffer.append("dest_ip_data_City:").append(dest_ip_data_City).append(", ");
		buffer.append("dest_ip_data_Lat:").append(dest_ip_data_Lat).append(", ");
		buffer.append("dest_ip_data_Long:").append(dest_ip_data_Long).append(", ");
		buffer.append("dest_ip_data_Timestamp:").append(dest_ip_data_Timestamp).append(", ");
		buffer.append("source_geo:").append(dest_geo).append(", ");
		buffer.append("dest_geo:").append(source_geo).append(", ");


		return buffer.toString();
	}

}
