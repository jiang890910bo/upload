/*    */ package com.j1.util;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.csource.common.NameValuePair;
/*    */ 
/*    */ public final class NameValueConvert
/*    */ {
/*    */   public static NameValuePair[] toNameValuePairs(Map<String, String> map)
/*    */   {
/* 10 */     NameValuePair[] nameValuePairs = new NameValuePair[map.size()];
/* 11 */     int i = 0;
/* 12 */     for (String key : map.keySet()) {
/* 13 */       nameValuePairs[(i++)] = new NameValuePair(key, (String)map.get(key));
/*    */     }
/* 15 */     return nameValuePairs;
/*    */   }
/*    */ }
