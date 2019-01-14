package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;


public class Controller {
    private long summ = 0;
    private long found = 0;
    private int page = 0;
    private int count = 0;
    private ArrayList<String> urlVacancies = new ArrayList<>();
    private ArrayList<Vacancy> vacanciesList = new ArrayList<>();
    @FXML
    private Button startButton;
    @FXML
    private Label textLabel;

    @FXML
    void initialize() {
        startButton.setOnAction(e -> {
            textLabel.setText("Start!");
            count = 0;
            long timeStart = System.currentTimeMillis();
            readText();
            getVacanciesJson();
            textLabel.setText("\nКоличество вакансий: " + summ);
            new ParserVacancies(vacanciesList);
            textLabel.setText(textLabel.getText() + "\nЗатраченое время:" +
                    ((double) (System.currentTimeMillis() - timeStart) / 1000) + "\nDone!");
            System.out.println("Всего запрошено организаций: " + count);
        });
    }

    private void readText() {
        try {
            FileInputStream fstream = new FileInputStream("file.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String string;
            while ((string = br.readLine()) != null) {
                getJsonOrg(string);
                count++;
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла с id!");
        }
    }

    private void getJsonOrg(String id) {
        try {
            URL url = new URL("https://api.hh.ru/vacancies?employer_id=" + id + "&area=1&area=2088&per_page=100&page=" + page);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            int code = connection.getResponseCode();
            System.out.print(id + ":" + code);
            if (code == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "utf8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    summVacancyAndUrl(line);
                }
                reader.close();
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (((page + 1) * 100) < found) {
            page++;
            getJsonOrg(id);
        } else {
            page = 0;
            return;
        }
    }

    private void summVacancyAndUrl(String json) {
        try {
            JSONObject org = (JSONObject) JSONValue.parseWithException(json);
            Long foundL = (Long) org.get("found");
            System.out.println(":" + foundL);
            if (foundL == 0) return;
            if (page == 0) {
                summ += foundL;
            }
            found = foundL;
            JSONArray vacancies = (JSONArray) org.get("items");
            JSONObject vacancy;
            for (int i = 0; i < vacancies.size(); i++) {
                vacancy = (JSONObject) vacancies.get(i);
                urlVacancies.add((String) vacancy.get("url"));
            }
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }

    private void getVacanciesJson() {
        try {
            for (int i = 0; i < urlVacancies.size(); i++) {
                URL url = new URL(urlVacancies.get(i));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setUseCaches(false);

                int code = connection.getResponseCode();

                if (code == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream(), "utf8"));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        getDataVacancy(line);
                    }
                    reader.close();
                }
                connection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void getDataVacancy(String line) {
        JSONObject jsonVacanscy = null;
        Vacancy vacancy = new Vacancy();
        try {
            jsonVacanscy = (JSONObject) JSONValue.parseWithException(line);
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Ошибка парсинга вакансии!");
        }
        if ((boolean) jsonVacanscy.get("archived")) {
            System.out.println("Вакансия в архиве!!!!!!!!!!!!!!");
            return;
        }
        //Наменование
        vacancy.setName((String) jsonVacanscy.get("name"));
        //Организация
        JSONObject jsonOrg = (JSONObject) jsonVacanscy.get("employer");
        if (jsonOrg != null) {
            vacancy.setOrganization((String) jsonOrg.get("name"));
            vacancy.setURLOrganization((String) jsonOrg.get("alternate_url"));
        }
        //Зарпата
        JSONObject jsonSalary = (JSONObject) jsonVacanscy.get("salary");
        if (jsonSalary != null) {
            vacancy.setSalaryFrom((Long) jsonSalary.get("from"));
            vacancy.setSalaryTo((Long) jsonSalary.get("to"));
            vacancy.setGross((Boolean) jsonSalary.get("gross"));
        }
        //График работы
        JSONObject jsonSchedule = (JSONObject) jsonVacanscy.get("schedule");
        if (jsonSchedule != null) {
            vacancy.setSchedule((String) jsonSchedule.get("name"));
        }
        //Тип занятости
        JSONObject jsonEmployment = (JSONObject) jsonVacanscy.get("employment");
        if (jsonSchedule != null) {
            vacancy.setEmployment((String) jsonEmployment.get("name"));
        }
        //Опыт работы
        JSONObject jsonExperience = (JSONObject) jsonVacanscy.get("experience");
        if (jsonExperience != null) {
            vacancy.setExperience((String) jsonExperience.get("name"));
        }
        //Описание
        vacancy.setDescription((String) jsonVacanscy.get("description"));
        //Ключевые навыки
        JSONArray jsonKey_skills = (JSONArray) jsonVacanscy.get("key_skills");
        if (jsonKey_skills != null) {
            ArrayList<String> skillList = new ArrayList<>();
            JSONObject jsonSkill;
            for (int i = 0; i < jsonKey_skills.size(); i++) {
                jsonSkill = (JSONObject) jsonKey_skills.get(i);
                skillList.add((String) jsonSkill.get("name"));
            }
            vacancy.setKey_skills(skillList);
        }
        //Специализация
        JSONArray jsonSpecializations = (JSONArray) jsonVacanscy.get("specializations");
        if (jsonSpecializations != null) {
            JSONObject jsonSpecialization;
            LinkedHashSet<String> lhs = new LinkedHashSet<>();
            for (int i = 0; i < jsonSpecializations.size(); i++) {
                jsonSpecialization = (JSONObject) jsonSpecializations.get(i);
                lhs.add((String) jsonSpecialization.get("profarea_name"));
            }
            vacancy.setSpecializations(lhs);
        }
        //Контакты
        JSONObject jsonContacts = (JSONObject) jsonVacanscy.get("contacts");
        if (jsonContacts != null) {
            vacancy.setEmail((String) jsonContacts.get("email"));
        }

        JSONObject jsonAddress = (JSONObject) jsonVacanscy.get("address");
        if (jsonAddress != null) {
            vacancy.setCity((String) jsonAddress.get("city"));
            vacancy.setStreet((String) jsonAddress.get("street"));
            vacancy.setBuilding((String) jsonAddress.get("building"));

            if (jsonAddress.get("lat") != null) {
                vacancy.setLat((double) jsonAddress.get("lat"));
            }
            if (jsonAddress.get("lng") != null) {
                vacancy.setLon((double) jsonAddress.get("lng"));
            }
        }
        vacanciesList.add(vacancy);
    }
}
