package test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;


public class test1 {
	public String a;
	public String getA() {
		return a;
	}
	public void setA(String a) {
		this.a = a;
	}
	public static void main(String[] args) {
		
		
		String str="11111111111111";
		if(str!=null && str.length()>=10   ){
			String  str2= str.replaceAll( str.substring(0, 6),"******");
			System.out.println(str.replaceAll( str.substring(0, 6),"******"));
			System.out.println(str2.replaceAll( str.substring(str.length()-4, str.length()),"****"));
		}
		

	String s1="温馨提示：亲爱的?AgentName?伙伴，您的客户?AppntName?所递交的理赔号?Clmno?保单号为?Contno?的在线理赔申请已收到，我们会尽快审核，如材料不全将通知其补交材料，您可登录公司网站或致电95105768查询理赔进展情况。";
	InputStream is=null;
	ByteArrayOutputStream baos =null;
	try {
		
		Blob b = new SerialBlob(s1.getBytes("UTF-8"));
		
		byte[] baa= new byte[1024];
		is= b.getBinaryStream();
		System.out.println(is.read());
		
		baos= new ByteArrayOutputStream();
		 byte[] buffer = new byte[1024]; // 1KB
		 int len = -1;
		 while ((len = is.read(buffer)) != -1) { //当等于-1说明没有数据可以读取了
             baos.write(buffer, 0, len);   //把读取到的内容写到输出流中
         }
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
	

	String sql_1 = "SELECT "+
			"'' BRANCH_DESC," +/*1*机构描述*/ 
	        
			"lcp.managecom," +/*2*机构*/
			
			"'' CHANNEL_DESC," +/*3*渠道描述*/
			 
			"lcp.salechnl," +/*4*渠道*/
			
			"llr.rgtno," +/*5*流水号*/
			
			"llr.rgttype," +/*6*案件类型*/
			 
			"ail.contno," +/*7*保单号*/
			 
			"llc.customername," +/*8*被保人姓名*/
			 
			"llc.idtype," +/*9*被保人证件号码类型*/
			 
			"llc.idno," +/*10*被保人证件号码*/
			 
			"llr.applydate," +/*11*申请日期*/
			 
			"llr.rgtdate," +/*12*立案日期*/
			 
			"llr.endcasedate," +/*13*结案日期*/
			 
			"llr.accidentdate," +/*14*事故日期*/
			
			"lcp.riskcode," +/*15*险种代码*/
			
			"'' PROD_IDNTFR," +/*16*保项id 为空*/
			 
			"ail.StandPay CLAIM_AMOUNT," +/*17*保项申请额 理算金额*/
			 
			"ail.RealPay REAL_PAY_AMOUNT," +/*18*实际赔付金额*/
			 
			"uwm.auditidea CLAIM_JUDGE," +/*19*审核意见为审批人看的，审核备注给客户看*/
			 
			"'' CREATE_TIME," + /*20*创建日期*/
			
			"'' CREATE_BY," + /*21*创建人*/
			
			"'' COL_SEQ," +/*22*序列号*/
			 
			"'' COL_YMTH," +/*33*批准年月日*/
			 
			"'' SOURCE," +/*24*结案or未结案*/
			 
			"'' CVRG_CLAIM_DAYS," +/*25*住院天数*/
			 
			"'' EVENT_DESC," +/*26*理赔类型*/
			 
			"llr.accidentreason FF_DESC," +/*27*出险原因*/
			 
			"llc.AccClassNo CASE_TYPE," +/*28*事故分类*/
			 
			"llc.AccDetailNo CASE_DESC," +/*29*事故细项*/
			 
			"llc.IllnessName ITEM_DESC, " +/*30*疾病名称*/
			
			"AIL.POLNO POLNO, " +/*31*险种号码*/
			
			"AIL.getdutykind getdutykind, " +/*32*险种号码*/
			
			"AIL.givetype givetype " +/*31*给付责任层给付决定04拒付*/
			 
			" FROM "
			+ " llclaim cla LEFT JOIN llregister llr ON cla.clmno = llr.rgtno "
				
			+ " LEFT JOIN llclaimuwmain uwm ON llr.rgtno = uwm.clmno "
					
			+ " LEFT JOIN llcase llc ON llr.rgtno = llc.caseno "
						
			+ " LEFT JOIN lccont lcc ON llc.CONTNO = lcc.contno "
							
			+ " LEFT join llclaimdetail ail on ail.clmno = cla.clmno "
								
			+ " LEFT JOIN lcpol lcp ON ail.polno = lcp.polno  "
									
			+ " WHERE "
			+ "cla.StandPay IS NOT NULL "
			+ "AND lcc.managecom IS NOT NULL "/**加匹配理算后表的数据不为空是因为得到的数据需要是匹配理算过的结案或未结案*/
			+ "and ail.clmno is not null "
			+ "and cla.clmno is not null "
			+ "and llr.rgtno is not null "
			+ "and uwm.clmno is not null "/**核赔表数据不能为空*/
			+ "and llc.caseno is not null "
			+ "and AIL.givetype <> '04' "/**给付层不能为拒付*/
			+ "and lcp.polno is not null ";
			
			System.out.println(sql_1);
			
			
			
			
			
			
			
			
			
			
			String sqls = "SELECT IFNULL(SUM(billmoney - RefuseAmnt), 0) FROM llfeemain WHERE"//ysq 20190325 免赔额减免的是用户需要承担的费用，所以要除去不合理金额
					+ " clmno IN ( SELECT DISTINCT lc.clmno FROM llclaim lc, llclaimdetail ld WHERE"
					+ " lc.rgtno = ld.rgtno"
					+ " AND ld.polno = '?polno?' AND lc.clmstate IN ('50') and lc.givetype='0'"
					+ " AND EXISTS ( SELECT caseno FROM llcase c WHERE c.caseno = lc.clmno"
					+ " AND accdate >= add_months ( to_date ('?Cvalidate?', 'yyyy-mm-dd'), ("
			   		+ "?INTERVAL?"
			   		+ "- 1) * 12 ) AND accdate <= add_months ( to_date ('"
			   		+ "?Cvalidate?"
			   		+ "', 'yyyy-mm-dd'), ?INTERVAL?*12)))"
					+ " AND (( feetype = '1' AND invoicetype <> '4' ) OR ( feetype = '2' AND invoicetype <> '2' ))"; 
			System.out.println( sqls);
			
			
			String  sql1111 = "SELECT t.polno, t.getdutycode,sum(t1.fee)"
					+ " FROM lltoclaimdutyfee t, llcasereceipt t1"
					+ " WHERE t.clmno = '?ClmNo?'"
					+ " AND t.mainfeeno = t1.mainfeeno AND t.dutyfeestano = t1.feedetailno"
					+ " AND t.dutyfeecode = t1.feeitemcode AND t.dutyfeetype = t1.feeitemtype"
					+ "	and t.invoicetype = concat('S',t1.invoicetype)"
					+ " and riskcode IN ('tcMB03','TCMB04')"
					+ " and polno = '?polno?'"
					+ " GROUP BY t.polno, t.getdutycode";
			
			System.out.println(sql1111);
			
/*			String doublee="-5983.600";
			double b =Double.parseDouble(doublee);
			double a =Double.parseDouble("1");
			System.out.println(a-b);
			*/
			BigDecimal bd=new BigDecimal("10000");
			BigDecimal bd2=new BigDecimal("-5983.600");
			System.out.println(bd.subtract(bd2));
			
			
			  String strss = "/total/one";
		        String temp = strss.substring(strss.lastIndexOf("/")+1,strss.length());
		        System.out.println(temp);
		        
		        
		        
		        List<Integer> list =new ArrayList<Integer>();
		        
		        for( int i=1 ;i<=10 ;i++){
		        	boolean b =true;
		        	for(int j =1 ;j<=10 ;j++){
		        		if(i==6){
		        			b=false;
		        			break;
		        		}
		        	}
		        	if(b){
		        		list.add(i);
		        	}
		        	
		        }
		        
		        list.size();
		        
		        test1 t=new test1(); 
		        System.out.println((String)t.getA()+"1");
		        
		        
				String tmpString=
						"<fileDetail>"
								+ "<filed name=\"FILENAME\" type=\"String\">123</filed>"
							   + "<filed name=\"BASE64EncoderFileDetail\" type=\"String\">123</filed>"
						+"</fileDetail>";
				String s=
						"<fileDetail>"
								+ "<filed name=\"FILENAME\" type=\"String\">456</filed>"
							   + "<filed name=\"BASE64EncoderFileDetail\" type=\"String\">456</filed>"
						+"</fileDetail>";
		        
				System.out.println(tmpString+=s);
				
				
				
				
				String baseSql3 = "select t.appntname name,t.appntsex sex,date_format(t.appntbirthday,'%Y-%m-%d') birthday,t.idtype,t.idno,'1' custRole,'0' benftRate,'' bnftype,  "
						+ " (select CONCAT(nvl(func_get_address_name(t2.province,'01'),nvl(t2.province,'')),nvl(func_get_address_name(t2.city,'02'),nvl(t2.city,'')),nvl(func_get_address_name(t2.county,'03'),nvl(t2.county,'')),nvl(t2.Street,''),nvl(t2.HouseNumber,'')) address from LCAddress t2 where t2.customerno = t.appntno and t2.addressno = t.addressno) address,"
						+ " (select t2.zipcode from LCAddress t2 where t2.customerno = t.appntno and t2.addressno = t.addressno) zipcode,"
						+ " (select t2.mobile from LCAddress t2 where t2.customerno = t.appntno and t2.addressno = t.addressno) mobile,TIMESTAMPDIFF(year,t.appntbirthday,(select s.cvalidate from lccont s where s.contno = t.contno)) age,"
						+ " (select t.codename from ldcode t where t.codetype = 'sex' and t.code = t.appntsex) sexCH,"
						+ " (select t.codename from ldcode t where t.codetype = 'idtype' and t.code = t.idtype) idtypeCH,t.appntno custCode,'' bnfno,date_format(t.IDExpDate,'%Y-%m-%d') IDExpDate,t.SocialInsuFlag,"
						+ " (select ss.codename from ldcode ss where ss.codetype = 'SocialInsuFlag' and ss.code = t.SocialInsuFlag) SocialInsuFlagCH,"
						+ " max('') insuredno "
						+ " from LCAppnt t "
						+ " where t.contno = '?contno?'"
						+ " UNION "
						+ " select t.name,t.sex,date_format(t.birthday,'%Y-%m-%d') birthday,t.idtype,t.idno,'2' custRole,'0' benftRate,'' bnftype, "
						+ " (select CONCAT(nvl(func_get_address_name(t2.province,'01'),nvl(t2.province,'')),nvl(func_get_address_name(t2.city,'02'),nvl(t2.city,'')),nvl(func_get_address_name(t2.county,'03'),nvl(t2.county,'')),nvl(t2.Street,''),nvl(t2.HouseNumber,'')) address from LCAddress t2 where t2.customerno = t.insuredno and t2.addressno = t.addressno) address,"
						+ " (select t2.zipcode from LCAddress t2 where t2.customerno = t.insuredno and t2.addressno = t.addressno) zipcode,"
						+ " (select t2.mobile from LCAddress t2 where t2.customerno = t.insuredno and t2.addressno = t.addressno) mobile,"
						+ " (select s.insuredappage from lcpol s where s.contno = t.contno and s.polno = s.mainpolno and s.appflag not in ('03','99','9','05')) age,"
						+ " (select t.codename from ldcode t where t.codetype = 'sex' and t.code = t.sex) sexCH,"
						+ " (select t.codename from ldcode t where t.codetype = 'idtype' and t.code = t.idtype) idtypeCH,t.insuredno custCode,'' bnfno,date_format(t.IDExpDate,'%Y-%m-%d') IDExpDate,t.SocialInsuFlag,"
						+ " (select ss.codename from ldcode ss where ss.codetype = 'SocialInsuFlag' and ss.code = t.SocialInsuFlag) SocialInsuFlagCH, "
						+ "  max('') insuredno "
						+ " from LCInsured t "
						+ " where t.contno = '?contno?'"
						+ " UNION"
						+ " (select t.name,t.sex,date_format(t.birthday,'%Y-%m-%d') birthday,t.idtype,t.idno,"
						+ "		max('3') custRole,"
						+ "		max(0+cast((t.BnfLot*100) AS CHAR)) benftRate,"
						+ "		max(t.bnftype), "
						+ "		max(nvl('','')) address,"
						+ "		max((select t2.zipcode from LCAddress t2 where t2.customerno = t.customerno ORDER BY t2.addressno desc LIMIT 1)) zipcode,"
						+ "		max((select t2.mobile from LCAddress t2 where t2.customerno = t.customerno ORDER BY t2.addressno desc LIMIT 1)) mobile,"
						+ "		max('') age,"
						+ "		max((select t.codename from ldcode t where t.codetype = 'sex' and t.code = t.sex)) sexCH,"
						+ "		max((select t.codename from ldcode t where t.codetype = 'idtype' and t.code = t.idtype)) idtypeCH,"
						+ "		max(t.customerno) custCod,"
						+ "		max(t.BnfGrade),"
						+ "		max(date_format(t.IDExpDate,'%Y-%m-%d')) IDExpDate,"
						+ "		max('') SocialInsuFlag,"
						+ "		max('') SocialInsuFlagCH,"
						+ "		max(t.insuredno) insuredno "
						+ " from LCBnf t "
						+ " where t.contno = '?contno?' GROUP BY t.insuredno,t.name,t.sex,t.birthday,t.idtype,t.idno ORDER BY t.BnfGrade)";
				
				System.out.println(baseSql3.toString());
				
				String strssss="江苏省苏州市常熟 市东区话 区";
				
				System.out.println(strssss.contains("区"));
				System.out.println(strssss.indexOf("区"));
				int p=strssss.indexOf("区");
				System.out.println(strssss.substring(0,p+1));
				
				String strssss1="江苏省";
				System.out.println(strssss1.substring(0,3));
				String mRemarks="hello"+"hy";
				System.out.println(mRemarks);
				
				String commbankaccno ="12345678910";
				String commbankaccnoq =  commbankaccno.substring(0, 6);
				String commbankaccnoh =commbankaccno.substring(commbankaccno.length()-4, commbankaccno.length());
				System.out.println(commbankaccnoq);
				System.out.println(commbankaccnoh);
				
				
				String tSql8 = "SELECT DISTINCT tt.riskcode, tt.polno, tt.getdutykind, tt.customerno, tt.expandflag, tt.code1, t2.cvalidate FROM"
						+ " ( SELECT t1.riskcode, t1.polno, t1.getdutykind, t1.customerno, t1.expandflag, "
						+ "( CASE WHEN t.code1 IS NOT NULL THEN cast(t.code1 AS SIGNED) ELSE 99 END ) code1,t.othersign FROM "
						+ "lltoclaimduty t1, ldcode1 t WHERE"
						+ " t1.riskcode = t. CODE AND t.codetype LIKE 'decriskorder' AND t1.ClmNo = '?ClmNo?' ) tt, lcpol t2 WHERE"
						+ " 1 = 1 AND t2.polno = tt.polno"
						+ " ORDER BY cast(tt.code1 AS SIGNED),cast(tt.othersign as signed), CAST(tt.expandflag AS signed), t2.cvalidate";
				System.out.println(tSql8);
				
				String tSql = "select currency,(case when sum(standpay) is null then 0 else sum(standpay) end) from LLClaimDetail where 1 = 1 "
						+ " and ClmNo = '" + "?ClmNo?" + "'" + " and GetDutyKind = '"
						+ "?GetDutyKind?" + "'" 
						+" group by currency ";
				System.out.println(tSql);
				
				
				
/*				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");

				Calendar c = Calendar.getInstance();

				c.add(Calendar.MONTH, -1);

				c.set(Calendar.DAY_OF_MONTH,1);//1:本月第一天

				String day1= format.format(c.getTime());

				System.out.println("本月第一天:"+day1);*/

				//获取当前月最后一天

/*				Calendar ca = Calendar.getInstance();
				ca.add(Calendar.MONTH, -1);

				ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));

				String day2= format.format(ca.getTime());

				System.out.println("本月最后一天:"+day2);
*/
				String sqlString = "select t.OtherNo,t3.Prep3,t2.SerPersonNo"
						+ " from lsmlettermainb t ,lsmletterperson t2,lsmxmldeploy t3"
						+ " where t.SerialNo = t2.SerialNo"
						+ " and t2.XmlNo = t3.XmlId"
						+ " and t.OtherNoType = '2'"
						+ " and t2.LetterType = '5'"
						+ " and t2.PrintState in ('0','3')"
						+ " and (t2.IsHangUp <> '0' or t2.IsHangUp is null)"
						+ " and t2.PrintDate <= now()";
				
				System.out.println(sqlString);
				
				SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
				Date pase;
				try {
					pase = simpleDateFormat.parse("2019-11-13");
					System.out.println("三天后的日期：" + simpleDateFormat.format(new Date(pase.getTime() + (long)3 * 24 * 60 * 60 * 1000)));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
/*				Calendar calendar2 = Calendar.getInstance();
		        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		        String endDate= sdf2.format(calendar2.getTime());
		        calendar2.add(Calendar.DATE, -4);
		        String three_days_after = sdf2.format(calendar2.getTime());
		        
		        System.out.println(endDate+" #####   "+three_days_after);*/
		        

				String xx89="<ClaimInfo> "
		+	"  <CaseState>2</CaseState>  "
		+	"	  <RegisterInfo> "
		+	"	    <ClaimReasonList> "
				+	"      <RgtNo>05910000110</RgtNo>  "
		+	"	      <ReasonCode>200</ReasonCode>  "
		+	"	      <ContNo>0014988207</ContNo>  "
		+	"	      <Source>3</Source> "
				+	"	    </ClaimReasonList>  "
		+	"	    <RegisterApply> "
			+	"      <RptorName>林秀琼</RptorName>  "
				+	"      <AppSex>2</AppSex>  "
			+	"      <AppIdtype>111</AppIdtype>  "
			+	"      <AppIdno>350302196703150328</AppIdno>  "
				+	"	      <AppIDExpDatehidden/>  "
			 +	"     <CommonAddress>福建省莆田市荔城区拱辰街道陡门村东郊自然村28号</CommonAddress>  "
			   +	"   <Relation>00</Relation>  "
			  +	"    <AccDate>2019-09-23</AccDate>  "
			  +	"    <AccType>2</AccType>  "
			 +	"     <RptorPhone>13860900879</RptorPhone>  "
				+	"    <AppntZipCode/>  "
			  +	"    <AccPlace/> "
				+	"   </RegisterApply>  "
			    +	"   <CaseInfo> "
			   +	"   <OtherAccidentDate>2019-09-23</OtherAccidentDate>  "
			    +	"  <AccDesc>面神经麻痹</AccDesc>  "
			   +	"   <TreatAreaName>莆田学院附属医院</TreatAreaName>  "
			    +	"  <IllnessCode>G51.9</IllnessCode>  "
			   +	"   <IllnessName>面神经疾患，未特指</IllnessName>  "
			   +	"   <IllnessRemark>面神经麻痹</IllnessRemark> "
			   +	" </CaseInfo>  "
			  +	"  <InHospitalListInfo> "
			   +	"   <ClinicMainFeeNo>00455319</ClinicMainFeeNo>  "
			   +	"   <ClinicHosName>莆田学院附属医院</ClinicHosName>  "
			   +	"   <CityEmpMedReimburse1>2786.49</CityEmpMedReimburse1>  "
			  +	"    <CityResiMedReimburse1>0</CityResiMedReimburse1>  "
			   +	"   <NewRuralCoopMedReimburse1>0</NewRuralCoopMedReimburse1>  "
			   +	"   <OtherSociInsuReimburse1>0</OtherSociInsuReimburse1>  "
			    +	"  <OtherPayRecevied1>0</OtherPayRecevied1>  "
				+	"   <InvoiceType>1</InvoiceType>  "
			     +	" <ClinicStartDate>2019-09-23</ClinicStartDate>  "
			   +	"   <ClinicEndDate>2019-09-09</ClinicEndDate>  "
			   +	"   <InHosSeriousStartDate/>  "
			     +	" <InHosSeriousEndDate/>  "
			    +	"  <Remark>租用临时床48元;梅毒螺旋体特异抗体测定39元;</Remark>  "
			    +	"  <ReceiptList> "
			    +	"    <FeeItemName>治疗费</FeeItemName>  "
			   +	"     <Fee>3041.64</Fee>  "
			    +	"    <SelfAmnt>0</SelfAmnt>  "
			   +	"     <SelfPayFee>0</SelfPayFee>  "
			       +	" <RefuseAmnt>0.00</RefuseAmnt> "
			   +	"   </ReceiptList>  "
			   +	"   <ReceiptList> "
			     +	"   <FeeItemName>西药费</FeeItemName>  "
			       +	" <Fee>675.07</Fee>  "
			      +	"  <SelfAmnt>0</SelfAmnt>  "
			     +	"   <SelfPayFee>0</SelfPayFee>  "
			    +	"    <RefuseAmnt>0.00</RefuseAmnt> "
			  +	"    </ReceiptList>  "
			     +	" <ReceiptList> "
				+	"    <FeeItemName>床位费</FeeItemName>  "
			       +	" <Fee>688.00</Fee>  "
			      +	"  <SelfAmnt>48.00</SelfAmnt>  "
			       +	" <SelfPayFee>0</SelfPayFee>  "
			     +	"   <RefuseAmnt>0.00</RefuseAmnt> "
			    +	"  </ReceiptList>  "
				+	" <ReceiptList> "
			      +	"  <FeeItemName>诊疗费</FeeItemName>  "
			    +	"    <Fee>301.00</Fee>  "
			       +	" <SelfAmnt>0</SelfAmnt>  "
				+	"  <SelfPayFee>0</SelfPayFee>  "
			      +	"  <RefuseAmnt>0.00</RefuseAmnt> "
			     +	" </ReceiptList>  "
			     +	" <ReceiptList> "
			      +	"  <FeeItemName>化验费</FeeItemName>  "
			    +	"    <Fee>1321.00</Fee>  "
			   +	"     <SelfAmnt>39.00</SelfAmnt>  "
			      +	"  <SelfPayFee>0</SelfPayFee>  "
			       +	" <RefuseAmnt>0.00</RefuseAmnt> "
			     +	" </ReceiptList>  "
			     +	" <ReceiptList> "
			      +	"  <FeeItemName>护理费</FeeItemName>  "
			      +	"  <Fee>400.00</Fee>  "
			     +	"   <SelfAmnt>0</SelfAmnt>  "
			      +	"  <SelfPayFee>0</SelfPayFee>  "
			      +	"  <RefuseAmnt>0.00</RefuseAmnt> "
			      +	"</ReceiptList>  "
			    +	"  <ReceiptList> "
				+	"    <FeeItemName>检查费</FeeItemName>  "
			     +	"   <Fee>803.00</Fee>  "
				+	"     <SelfAmnt>0</SelfAmnt>  "
			    +	"    <SelfPayFee>0</SelfPayFee>  "
				+	"      <RefuseAmnt>0.00</RefuseAmnt> "
			  +	"    </ReceiptList> "
			  +	"  </InHospitalListInfo> "
			 +	" </RegisterInfo>  "
			+	"  <ClaimCalcInfo/>  "
			+	"  <Calculate/>  "
		+	"	  <BenfInfoList> "
			  +	"  <RelationToPayee>00</RelationToPayee>  "
			  +	"  <PayeeName>林秀琼</PayeeName>  "
			 +	"   <PayeeSex>2</PayeeSex>  "
			  +	"  <PayeeBirthday>1967-03-15</PayeeBirthday>  "
				+	"   <PayeeIDType>111</PayeeIDType>  "
			  +	"  <PayeeIDNo>350302196703150328</PayeeIDNo>  "
			 +	"   <BankCode>中国农业银行</BankCode>  "
			 +	"   <BankAccNo>6228480692723331819</BankAccNo>  "
			 +	"   <AccName>林秀琼</AccName>  "
			+	"    <BankAccNoAddress>福建省</BankAccNoAddress> "
			+	"  </BenfInfoList> "
		+	"	</ClaimInfo>";
				
				
				System.out.println(xx89);
				
			    Calendar calendar2 = Calendar.getInstance();
		        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		        String endDate= sdf2.format(calendar2.getTime());
		        calendar2.add(Calendar.DATE, -4);
		        String three_days_after = sdf2.format(calendar2.getTime());
		        System.out.println(three_days_after);
		        
		        StringBuilder sb = new StringBuilder("select  b.ContNo,b.OtherNo,b.OtherNoType, " +
		                "' ' letter_code," +
		                "c.LetterType, " +
		                "(select XmlName from lsmxmldeployb where XmlId=c.XmlNo) xmlName, " +
		                "c.LetterState,c.PrintDate,c.PrintState,c.ReplyState,c.PrintType, " +
		                "c.PrintSendProperty,c.ElecAddressURL " +
		                "from lccont a join lsmlettermainb b on (a.contno=b.ContNo) " +
		                "join lsmletterpersonb c on(b.SerialNo=c.SerialNo) " +
		                "where 1=1 ");
		        
		        System.out.println(sb.toString());
		        
		        
		        StringBuilder sb2 = new StringBuilder("select  b.ContNo,b.OtherNo,b.OtherNoType, " +
		                "(select m.codeContent from lsmcodemapping m join lsmxmldeploy n on (m.codeNo = n.XmlCode)" +
		                "where n.XmlId = c.XmlNo) letter_code," +
		                "c.LetterType, " +
		                "(select XmlName from lsmxmldeploy where XmlId=c.XmlNo) xmlName, " +
		                "c.LetterState,c.PrintDate,c.PrintState,c.ReplyState,c.PrintType, " +
		                "c.PrintSendProperty,c.ElecAddressURL " +
		                "from lccont a join lsmlettermainb b on (a.contno=b.ContNo) " +
		                "join lsmletterperson c on(b.SerialNo=c.SerialNo) " +
		                "where 1=1 "
		                + " and c.PrintState not in ('4','5') ");
		        
		        System.out.println(sb2.toString());
		        
				String sql1="select  DISTINCT aa.* from ("
						+ " select appntno from lccont  where  APPFLAG ='01' and  appntno ='?CustomerNo?'  "
						+ " union "
						+ " select insuredno from lccont  where  APPFLAG ='01' and  appntno ='?CustomerNo?'  "
						+ " union "
						+ " select appntno from lccont  where  APPFLAG ='01' and  insuredno ='?CustomerNo?'  "
						+ " union "
						+ " select insuredno from lccont  where  APPFLAG ='01' and  insuredno ='?CustomerNo?' "
						+ " ) aa "
						+ "";
				
				System.out.println(sql1.toString());
		        
			
	}
}
