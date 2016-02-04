/**
 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 limitations under the License.
 */

package hortonworks.hdp.refapp.ecm.ingestion.flume;

public class Constants {

    public static final String PROPERTY_PREFIX = "kafka";

    /* Properties */
    public static final String DEFAULT_TOPIC = "default-flume-topic";
    public static final String TOPIC = "topic";
    
    /* Header Properties */
    public static final String HEADER_KEY_DOC_CUST_NAME = "document.customer.name";
    public static final String HEADER_KEY_DOC_NAME = "document.name";
    public static final String HEADER_KEY_MIME_TYPE = "document.mime.type";
    public static final String HEADER_KEY_DOC_EXTENSION = "document.extension";
    public static final String HEADER_KEY_DOC_CLASS_TYPE = "document.class.type";
    
}
