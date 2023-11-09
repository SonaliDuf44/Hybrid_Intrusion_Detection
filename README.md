# Hybrid Intrusion Detection Syste with Focus on Signature Based Approach

## Requirements:
#### 1) Matlab
#### 2) Netbeans/Eclipse

## Contents:
#### 1) Dataset
##### --> training
##### --> test
#### 2) Models
##### --> Signature Based Model
##### --> Anomaly Based Model
#### 3) Sorces
##### --> Netbeans/Eclipse project source code
##### --> Import project in Netbeans/Eclipse 

### Abstract:
A Hybrid Intrusion Detection System (IDS) employs a dual-layer protection mechanism. The initial layer operates on a rule-based detection approach, while the second layer integrates a Supervised Learning model based on a Support Vector Machine (SVM) classifier.
The first layer, known as Rule-Based Detection, utilizes a database of malicious signatures to identify established attack patterns. These signatures encompass a range of characteristics, such as suspicious IP headers, packets with unauthorized TCP flag combinations, attempts at anonymous FTP attacks, DNS buffer overflow endeavors, and email subjects containing potential viruses.
The second layer incorporates an intrusion detection system that leverages an SVM classifier for the purpose of categorizing whether a given signature is indicative of a malicious activity. This classifier is trained on a dataset, such as the NSL-KDD dataset, enabling it to make informed decisions when classifying signatures based on the acquired knowledge.


### Credits:
> Sonali Dufare
