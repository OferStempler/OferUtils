package il.stempler.main;

/**
 * Created by ofer on 27/12/17.
 */
//import il.co.boj.mortgageMng.dao.MmLogActivityRepository;
//import il.co.boj.mortgageMng.model.MMRequest;
//import il.co.boj.mortgageMng.model.MMResponse;
//import il.co.boj.mortgageMng.model.MmLogActivity;
//import il.co.boj.mortgageMng.util.GeneralUtils;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//import java.util.function.Function;
//import java.util.function.Predicate;
//import java.util.function.UnaryOperator;
//
//import javax.annotation.PostConstruct;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import lombok.extern.log4j.Log4j;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.Signature;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.reflect.CodeSignature;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
//import org.springframework.stereotype.Component;
//
//@Component
//@ConditionalOnExpression("${logging.aspectlogger:true}") //
//@Aspect
//@Log4j
//public class ArroundAspect {
//
//	@Autowired
//	private MmLogActivityRepository	mmLogActivityRepository;
//
//	private ThreadLocal<Date> threadLocal = new ThreadLocal<>();
//	private Predicate<Object>     prd          = (o)->o instanceof HttpServletRequest || o instanceof HttpServletResponse;
//	private UnaryOperator<String> limitJsonStr = j-> j != null ? j.substring(0, Math.min(j.length(), 4000  ) ) : "";
//
//	//-----------------------------------------------------------------------------------------------------------------
//	 public ArroundAspect() {
//		 log.debug("ArroundAspect.CTOR()");
//	 }
//	//-----------------------------------------------------------------------------------------------------------------
//	 @PostConstruct
//	 public void init() {
//		 log.debug("ArroundAspect.init() - logging.aspectlogger=true");
//		 log.debug("                       To turn off this logging feature, set logging.aspectlogger=false in application.yml");
//	 }
//	//-----------------------------------------------------------------------------------------------------------------
//	 private void printSig(org.aspectj.lang.JoinPoint jp){
//			Signature sign = jp.getSignature();
//
//			log.info("\t\tMethod signature:"                   + sign );
//			log.info("\t\tMethod signature modifiers:"         + sign.getModifiers() ); //java.lang.reflect.Modifier
//			log.info("\t\tMethod signature DeclaringTypeName:" + sign.getDeclaringTypeName() );
//			log.info("\t\tMethod signature DeclaringType:"     + sign.getDeclaringType() );
//			//--------------------------------------------------------
//			MethodSignature methodSign = (MethodSignature)jp.getSignature();
//			Method method = methodSign.getMethod();
//			log.info("\t\tMethod name:[" + method.getName() + "]");
//
//			//--------------------------------------------------------
//	    	CodeSignature codeSignature = (CodeSignature) jp.getSignature();
//	    	String[] argNames = codeSignature.getParameterNames();
//			//--------------------------------------------------------
//			log.info("\t\tkind: "                              + jp.getKind() );
//			log.info("\t\tTarget: "                            + jp.getTarget().getClass().getSimpleName());
//			log.info("\t\tThis: "                              + jp.getThis().getClass().getSimpleName());
//
//			Object [] args = jp.getArgs();
//			if ( args != null ){
//				for (int i = 0; i < args.length; i++) {
//					if ( args[i] != null ){
//						log.info("\t\targ[" + i + "]:" /*+ args[i].getClass().getSimpleName() */ + argNames[i] + " = " + args[i]);
//					}
//				}
//			}else{
//				log.info("\t\tno args!");
//			}
//	 }
//	//-----------------------------------------------------------------------------------------------------------------
//	 private Function<Object [], String> concat = (arr)->{
//		 String s = null;
//		 StringBuilder sb = new StringBuilder();
//		 Arrays.stream( arr ).filter(prd.negate()).map((o)->o.toString()).forEach(sb::append);
//		 return sb.toString();
//	 };
//	//-----------------------------------------------------------------------------------------------------------------
//	public MmLogActivity externalLogsEnd(HttpServletRequest request, String _responseCode, String _responseMessage, String _responseJson ){
//
//		MmLogActivity mmLogActivity = (MmLogActivity) request.getAttribute("LOG");
//
//		if ( mmLogActivity != null ){
//			mmLogActivity.setResponseCode     ( _responseCode );
//			mmLogActivity.setClientIp         ( null );
//			mmLogActivity.setResponseJson     ( limitJsonStr.apply( _responseJson ) );
//			mmLogActivity.setTotalMilliseconds( System.currentTimeMillis() - mmLogActivity.getTotalMilliseconds() );
//
//			request.setAttribute("LOG", mmLogActivity);
//		}
//		return mmLogActivity;
//    }
//  //-----------------------------------------------------------------------------------------------------------------
//	 //@Around("execution(* il.co.boj.mortgageMng.controller.MortgageMngController.*(..))")
//	 public void doArroundAccessCheck(org.aspectj.lang.ProceedingJoinPoint jp) {
//  		 printSig(jp);
//		 try {
//			jp.proceed();
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
// 		printSig(jp);
//	 }
//    //-----------------------------------------------------------------------------------------------------------------
//	 @Before("execution(* il.co.boj.mortgageMng.controller.MortgageMngController*.*(..))")
//	 public void doB4AccessCheck(org.aspectj.lang.JoinPoint jp) {
//			// printSig(jp);
//			// Object [] args = jp.getArgs();
//			// argsScanner(args, initRow);
//		 threadLocal.set(new Date());
//	 }
//	//-----------------------------------------------------------------------------------------------------------------
//	 @AfterReturning(pointcut="execution(* il.co.boj.mortgageMng.controller.MortgageMngController*.*(..))", returning = "retObj")
//	 public void doAfterAccessCheck(org.aspectj.lang.JoinPoint jp, Object retObj) {
//			printSig(jp);
//			Date startTime = threadLocal.get();
//			Date endTime = new Date();
//			long elapsedTimeMillis = endTime.getTime() - ( startTime != null ? startTime.getTime() : new Date().getTime() );
//
////			1) Handle input auditing..
//			MmLogActivity mmLogActivity = auditInput(jp);
//			mmLogActivity.setTotalMilliseconds( elapsedTimeMillis );
//
////			2) Handle output auditing..
//			String replyJsonStr = GeneralUtils.convertJsonObjectToString(retObj);
//			log.debug("RET:" + replyJsonStr);
//			mmLogActivity.setResponseJson( limitJsonStr.apply( replyJsonStr ) );
//
////			3) Handle output logging from returned object..
//			if ( mmLogActivity.getResponseCode() == null || mmLogActivity.getResponseCode().length() == 0 ){
//				try {
//					setResponseCodeAndDescriptionFromRetObject(retObj, mmLogActivity);
//				} catch (IllegalArgumentException | IllegalAccessException e) {
//					log.error("Exception while calling setResponseCodeAndDescriptionFromRetObject() to set responseCode && responseDescription:[" + e.getMessage() + "]", e);
//					return;
//				}
//			}// if
//
////			4) Save logging entity..
//			mmLogActivityRepository.save(mmLogActivity);
//	 }
//	//-----------------------------------------------------------------------------------------------------------------
//	private void setResponseCodeAndDescriptionFromRetObject(Object retObj, MmLogActivity mmLogActivity) throws IllegalArgumentException, IllegalAccessException {
//		int foundCount = 0;
//		if ( retObj != null ){
//			if ( retObj instanceof MMRequest  || retObj instanceof MMResponse ){
//				Field [] fields = retObj.getClass().getDeclaredFields();
//				foundCount = getResponseFields(fields, retObj, mmLogActivity);
//
//				if ( foundCount < 2 && retObj.getClass().getSuperclass() != null ){
//					fields = retObj.getClass().getSuperclass().getDeclaredFields();
//					foundCount = getResponseFields(fields, retObj, mmLogActivity);
//				}
//			}else{ // retObj NOT instanceof MMResponse
//				if ( retObj instanceof List ){
//					if(((List<?>)retObj).size() == 0 ){
//						mmLogActivity.setResponseCode("108");
//						mmLogActivity.setMessageText("Record_Doesnt_Exist");
//
//					}else{
//						mmLogActivity.setResponseCode("0");
//						mmLogActivity.setMessageText("SUCCESS");
//					}
//				}else{ // retObj NOT instanceof List, Nor any response Entity..
//					mmLogActivity.setResponseCode("-1");
//					mmLogActivity.setMessageText("General failure - Undefined returned object of type:[" + retObj.getClass().getName() + "]");
//				}
//			}//else - retObj NOT instanceof MMResponse
//	    }// if ( retObj != null ){
//		else{
//			log.debug("retObj is null.");
//		}
//	}
//	//-----------------------------------------------------------------------------------------------------------------
//	private int getResponseFields(Field[] fields, Object retObj, MmLogActivity mmLogActivity) throws IllegalArgumentException, IllegalAccessException {
//		String fieldName = null;
//		String fieldVal  = null;
//		int foundCount = 0;
//		if ( fields != null && fields.length > 0 ){
//            for (Field field : fields) {
//            	fieldName = field.getName().toLowerCase();
//            	field.setAccessible(true);
//            	fieldVal = String.valueOf(  field.get(retObj) );
//            	setArgByNameToLogEntity(fieldVal, fieldName, mmLogActivity);
//			}
//
//		}else{
//		  return 0;
//		}
//		return foundCount;
//	}
//	//-----------------------------------------------------------------------------------------------------------------
//	private MmLogActivity auditInput(JoinPoint jp) {
//
//		MmLogActivity mmLogActivity = new MmLogActivity();
//
//		mmLogActivity.setCreated        ( new Date() );
//		mmLogActivity.setApplicationName( "MM" );
//
////		1) Get method name:
//		MethodSignature methodSign = (MethodSignature)jp.getSignature();
//		Method method = methodSign.getMethod();
//		String methodName = method.getName();
//		log.debug("\t\tMethod name:[" + methodName + "]");
//		mmLogActivity.setServiceName    ( method.getName() );
//		mmLogActivity.setModuleName("MM");
//
////		2) Handle request input fields..
//		mmLogActivity = setRowGenericFields(jp, mmLogActivity);
//
//		return mmLogActivity;
//	}
//	//-----------------------------------------------------------------------------------------------------------------
//	 private MmLogActivity setRowGenericFields(JoinPoint jp, MmLogActivity mmLogActivity){
//		 String  argName = null;
//
//		 CodeSignature codeSignature = (CodeSignature) jp.getSignature();
//		 String[] argNames = codeSignature.getParameterNames();
//
//		 Object [] args = jp.getArgs();
//		 boolean found = false;
//
//		 Object currArg = null;
//		 String jsonStr = null;
//		 if ( args != null ){
//			 for (int i = 0; i < args.length; i++) {
//				 currArg = args[i];
//				 if ( currArg != null ){
//					 log.debug("\t\tArg[" + i + "]:" + argNames[i] + ", value:[" + currArg + "]");
//					 argName = argNames[i].toLowerCase();
//					 found = setArgByNameToLogEntity(currArg, argName, mmLogActivity);
//					 if ( !found && currArg != null && currArg instanceof MMRequest){
//						 try {
//							jsonStr = GeneralUtils.convertJsonObjectToString( currArg );
//							jsonStr = limitJsonStr.apply( jsonStr );
//							mmLogActivity.setRequestJson( jsonStr );
//							setResponseCodeAndDescriptionFromRetObject(currArg, mmLogActivity);
//						} catch (IllegalArgumentException | IllegalAccessException e) {
//							log.error("ArroundAspect.setArgToLogEntity() - Exception:[" + e.getMessage() + "]", e);
//			            }
//					 }
//
//				 }//if
//			 }//for
//		 }else{
//			 log.debug("\t\tno args!");
//		 }
//		 return mmLogActivity;
//	 }
//	//-----------------------------------------------------------------------------------------------------------------
//	private boolean setArgByNameToLogEntity(Object currArg, String argName, MmLogActivity mmLogActivity) {
//		 boolean found = true;
//		 if ( String.class.isAssignableFrom( currArg.getClass() ) && ((String)currArg).equals("null") ){
//			 return false;
//		 }
//
//		 switch ( argName  ){
//		   case "idnumber":
//			 mmLogActivity.setIdNumber( (String) currArg );
//			 break;
//		   case "transactionid"  :
//			 mmLogActivity.setRequestGuid( (String) currArg );
//			 break;
//		   case "clientip"  :
//			 mmLogActivity.setClientIp( (String) currArg );
//			 break;
//		   case "channelname"  :
//			 mmLogActivity.setChannelName( (String) currArg );
//			 break;
//		   case "idtype"  :
//			 if ( currArg != null ){
//				 mmLogActivity.setIdType( (Integer) (String.class.isAssignableFrom( currArg.getClass() ) && ((String)currArg).matches("[0-9]+") ? Integer.valueOf((String)currArg) :
//					                            (Number.class.isAssignableFrom( currArg.getClass() ) ? currArg : null )
//					                        ));
//			 }
//			 break;
//		   case "userid"  :
//			 mmLogActivity.setUserName( (String) currArg );
//			 break;
//		   case "leadnum"  :
//		   case "leadnumber"  :
//		   case "leadid"  :
//			 mmLogActivity.setRequestCode( (String) currArg );
//			 break;
//		   case "step"  :
//			 mmLogActivity.setStep( (String) currArg );
//			 break;
//
//
//		   case "moduleName"  :
//			 mmLogActivity.setModuleName( (String) currArg );
//			 break;
//		   case "guid"  :
//			 mmLogActivity.setRequestGuid( (String) currArg );
//			 break;
//		   case "username"  :
//		   case "consultCode":
//		   case "crmconsultcode":
//			 if ( mmLogActivity.getUserName() == null ){
//				  mmLogActivity.setUserName( (String) currArg );
//			 }
//			 break;
//		   case "account"  :
//			 mmLogActivity.setAccount( (String) currArg );
//			 break;
//		   case "responsecode"  :
//			 mmLogActivity.setResponseCode( (String) currArg );
//			 break;
//		   case "messagetext"  :
//		   case "responsedescription":
//			 mmLogActivity.setMessageText( (String) currArg );
//			 break;
//		   case "request"  :
//			 if ( HttpServletRequest.class.isAssignableFrom(currArg.getClass())){
//				 mmLogActivity.setServerName( ((HttpServletRequest)currArg).getServerName() );
//				 mmLogActivity.setServerIp  ( ((HttpServletRequest)currArg).getRemoteHost() );
//			 }
//			 break;
//		   case "sessionid"  :
//			 mmLogActivity.setSessionId( (String) currArg );
//			 break;
//		   case "generaltext"  :
//			 mmLogActivity.setGeneralText( (String) currArg );
//			 break;
//		   default:
//			 log.debug("ArroundAspect.setArgToLogEntity() - no name matches argName:[" + argName + "]");
//			 found = false;
//		 }//switch
//
//		 return found;
//	}
//	//-----------------------------------------------------------------------------------------------------------------
//}