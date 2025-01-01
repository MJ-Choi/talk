import streamlit as st
import requests

# 외부 파일에서 API URL 읽기
def load_config():
  with open("config") as f:
    for line in f:
      if line.startswith("host"):
        return line.strip().split("=")[1]
  return ""

api_base_url = load_config()

st.title("TALK API Client")

menu = st.sidebar.selectbox("Select Menu", ["Front", "backOffice"])

if menu == "Front":
  action = st.sidebar.radio("Select Action", ["강연 목록", "강연 신청", "신청 내역 조회", "신청한 강연 취소", "실시간 인기 강연"])
  if action == "강연 목록":
    page = st.text_input("페이지 입력(정수 입력, min 0)")
    size = st.text_input("페이지 사이즈 입력(자연수 형태)")
    if st.button("조회"):
      response = requests.post(f"{api_base_url}?page={page}&size={size}")
      if response.status_code == 200:
        st.write("Response:", response.json())
      else:
        st.write("Error:", response.text)
  elif action == "강연 신청":
    mem_id = st.text_input("사번 입력(5자리)")
    talk_id = st.text_input("강연ID 입력(ex. T00001")
    if st.button("생성"):
      data = {"memId": mem_id, "talkId": talk_id}
      response = requests.post(f"{api_base_url}/register", json=data)
      if response.status_code == 200:
        st.write("Response:", response.json())
      else:
        st.write("Error:", response.text)
  elif action == "신청 내역 조회":
    mem_id = st.text_input("사번 입력")
    if st.button("조회"):
      response = requests.get(f"{api_base_url}/list/{mem_id}")
      if response.status_code == 200:
        st.write("Response:", response.json())
      else:
        st.write("Error:", response.text)
  elif action == "신청한 강연 취소":
    mem_id = st.text_input("사번 입력")
    talk_id = st.text_input("강연ID 입력")
    if st.button("신청"):
      data = {"memId": mem_id, "talkId": talk_id}
      response = requests.post(f"{api_base_url}/cancel", json=data)
      if response.status_code == 200:
        st.write("Response:", response.json())
      else:
        st.write("Error:", response.text)
  elif action == "실시간 인기 강연":
    page = st.text_input("페이지 입력(정수 입려. min 0)")
    size = st.text_input("페이지 사이즈 입력(자연수 형태)")
    if st.button("조회"):
      response = requests.get(f"{api_base_url}/popular?page={page}&size={size}")
      if response.status_code == 200:
        st.write("Response:", response.json())
      else:
        st.write("Error:", response.text)
elif menu == "backOffice":
  action = st.sidebar.radio("Select Action", ["강연 목록", "강연 등록", "강연ID로 강연 신청자 목록 조회", "강연 신청자 목록"])
  if action == "강연 목록":
    page = st.text_input("페이지 입력(정수 입력. min 0)")
    size = st.text_input("페이지 사이즈 입력(자연수 형태)")
    if st.button("조회"):
      response = requests.get(f"{api_base_url}/mgmt?page={page}&size={size}")
      if response.status_code == 200:
        st.write("Response:", response.json())
      else:
        st.write("Error:", response.text)
  elif action == "강연 등록":
    speaker = st.text_input("강연자 입력")
    place = st.text_input("강연장 입력")
    seat = st.text_input("신청 인원 입력(자연수 형태)")
    start_dtm = st.text_input("강연 시간(yyyy-MM-dd HH:mm 형식)")
    talk_desc = st.text_input("강연 설명 입력")
    if st.button("등록"):
      data = {"speaker": speaker, "place": place, "seat": seat, "startDtm": start_dtm, "desc": talk_desc}
      response = requests.post(f"{api_base_url}/mgmt/register", json=data)
      if response.status_code == 200:
        st.write("Response:", response.json())
      else:
        st.write("Error:", response.text)
  elif action == "강연ID로 강연 신청자 목록 조회":
    talk_id = st.text_input("강연ID")
    if st.button("조회"):
      response = requests.get(f"{api_base_url}/mgmt/list/{talk_id}")
      if response.status_code == 200:
        st.write("Response:", response.json())
      else:
        st.write("Error:", response.text)
  elif action == "강연 신청자 목록":
    page = st.text_input("페이지 입력(정수 입력. min 0)")
    size = st.text_input("페이지 사이즈 입력(자연수 형태)")
    if st.button("조회"):
      response = requests.get(f"{api_base_url}/mgmt/list?page={page}&size={size}")
      if response.status_code == 200:
        st.write("Response:", response.json())
      else:
        st.write("Error:", response.text)