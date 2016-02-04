package com.hortonworks.streaming.impl.kafka;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;

import backtype.storm.spout.Scheme;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NetflowScheme implements Scheme{

	private static final long serialVersionUID = -2990121166902741545L;

	private static final Logger LOG = Logger.getLogger(NetflowScheme.class);
	
	@Override
	public List<Object> deserialize(byte[] bytes) {
		
		String event = null;

		try {
			event = new String(bytes, "UTF-8");
			String[] pieces = event.split(",");

			if(pieces.length != 48) {
				LOG.error("Bad Netflow: " + event);
				//throw new RuntimeException("Bad number of fields: " + pieces.length);
				return new Values(null, null, null, null, null, null, null, 
				null, null, null, null, null, null, null, null, null, null, null, 
				null, null, null, null, null, null, null, null, null, null, 
				null, null, null, null, null, null, null, null, null, null, 
				null, null, null, null, null, null, null, null, null, null);
			}

			String nf_Start = String.valueOf(pieces[0]);
			String nf_End = String.valueOf(pieces[1]);
			Float nf_Duration = Float.valueOf(pieces[2]);
			String nf_SrcAddr = String.valueOf(pieces[3]);
			String nf_DstAddr = String.valueOf(pieces[4]);
			Long nf_SrdPort = Long.valueOf(pieces[5]);
			Long nf_DstPort = Long.valueOf(pieces[6]);
			String nf_Proto = String.valueOf(pieces[7]);
			String nf_flg = String.valueOf(pieces[8]);
			Long nf_fwd = Long.valueOf(pieces[9]);
			Long nf_stos = Long.valueOf(pieces[10]);
			Long nf_ipkt = Long.valueOf(pieces[11]);
			String nf_ibyt = String.valueOf(pieces[12]);
			String nf_opkt = String.valueOf(pieces[13]);
			Long nf_obyt = Long.valueOf(pieces[14]);
			Long nf_inIF = Long.valueOf(pieces[15]);
			Long nf_outIF = Long.valueOf(pieces[16]);
			Long nf_sAS = Long.valueOf(pieces[17]);
			Long nf_dAS = Long.valueOf(pieces[18]);
			Long nf_smk = Long.valueOf(pieces[19]);
			Long nf_dmk = Long.valueOf(pieces[20]);
			Long nf_dtos = Long.valueOf(pieces[21]);
			Long nf_dir = Long.valueOf(pieces[22]);
			String nf_NextHop = String.valueOf(pieces[23]);
			String nf_BGPNextHop = String.valueOf(pieces[24]);
			Long nf_svln = Long.valueOf(pieces[25]);
			Long nf_dvln = Long.valueOf(pieces[26]);
			String nf_ismc = String.valueOf(pieces[27]);
			String nf_odmc = String.valueOf(pieces[28]);
			String nf_idmc = String.valueOf(pieces[29]);
			String nf_osmc = String.valueOf(pieces[30]);
			String nf_mpls1 = String.valueOf(pieces[31]);
			String nf_mpls2 = String.valueOf(pieces[32]);
			String nf_mpls3 = String.valueOf(pieces[33]);
			String nf_mpls4 = String.valueOf(pieces[34]);
			String nf_mpls5 = String.valueOf(pieces[35]);
			String nf_mpls6 = String.valueOf(pieces[36]);
			String nf_mpls7 = String.valueOf(pieces[37]);
			String nf_mpls8 = String.valueOf(pieces[38]);
			String nf_mpls9 = String.valueOf(pieces[39]);
			String nf_mpls10 = String.valueOf(pieces[40]);
			Float nf_cl = Float.valueOf(pieces[41]);
			Float nf_sl = Float.valueOf(pieces[42]);
			Float nf_al = Float.valueOf(pieces[43]);
			String nf_RouterIP = String.valueOf(pieces[44]);
			String nf_eng = String.valueOf(pieces[45]);
			Long nf_exid = Long.valueOf(pieces[46]);
			String nf_tr = String.valueOf(pieces[47]);
			
			return new Values(nf_Start, nf_End, nf_Duration, nf_SrcAddr, nf_DstAddr, nf_SrdPort, nf_DstPort, 
				nf_Proto, nf_flg, nf_fwd, nf_stos, nf_ipkt, nf_ibyt, nf_opkt, nf_obyt, nf_inIF, nf_outIF, nf_sAS, 
				nf_dAS, nf_smk, nf_dmk, nf_dtos, nf_dir, nf_NextHop, nf_BGPNextHop, nf_svln, nf_dvln, nf_ismc, 
				nf_odmc, nf_idmc, nf_osmc, nf_mpls1, nf_mpls2, nf_mpls3, nf_mpls4, nf_mpls5, nf_mpls6, nf_mpls7, 
				nf_mpls8, nf_mpls9, nf_mpls10, nf_cl, nf_sl, nf_al, nf_RouterIP, nf_eng, nf_exid, nf_tr);
			
		} catch (Exception e) {
			LOG.error("Bad Netflow: " + event);
			//throw new RuntimeException(e);
			return new Values(null, null, null, null, null, null, null, 
				null, null, null, null, null, null, null, null, null, null, null, 
				null, null, null, null, null, null, null, null, null, null, 
				null, null, null, null, null, null, null, null, null, null, 
				null, null, null, null, null, null, null, null, null, null);
		}
		
	}

	@Override
	public Fields getOutputFields() {
		return new Fields("nf_Start", "nf_End", "nf_Duration", "nf_SrcAddr", "nf_DstAddr", "nf_SrdPort", "nf_DstPort", 
			"nf_Proto", "nf_flg", "nf_fwd", "nf_stos", "nf_ipkt", "nf_ibyt", "nf_opkt", "nf_obyt", "nf_inIF", "nf_outIF", 
			"nf_sAS", "nf_dAS", "nf_smk", "nf_dmk", "nf_dtos", "nf_dir", "nf_NextHop", "nf_BGPNextHop", "nf_svln", "nf_dvln", 
			"nf_ismc", "nf_odmc", "nf_idmc", "nf_osmc", "nf_mpls1", "nf_mpls2", "nf_mpls3", "nf_mpls4", "nf_mpls5", 
			"nf_mpls6", "nf_mpls7", "nf_mpls8", "nf_mpls9", "nf_mpls10", "nf_cl", "nf_sl", "nf_al", "nf_RouterIP", 
			"nf_eng", "nf_exid", "nf_tr");
	}
}