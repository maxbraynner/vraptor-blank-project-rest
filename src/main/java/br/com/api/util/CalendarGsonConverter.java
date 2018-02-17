package br.com.api.util;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

import javax.annotation.Priority;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Alternative;
import javax.interceptor.Interceptor;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

@Dependent
@Alternative
@Priority(Interceptor.Priority.LIBRARY_AFTER)
public class CalendarGsonConverter implements JsonDeserializer<Calendar>, JsonSerializer<Calendar>{

	@Override
	public Calendar deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		Calendar calendar = Calendar.getInstance();

		try {
			// verifica se é time
			if (Pattern.compile("\\d{4}.\\d{2}.\\d{2}.(\\d{2}.?)+").matcher(json.getAsString()).matches()) {
				calendar.setTime(getDateTimeFormat().parse(json.getAsString()));
			}else {
				calendar.setTime(getDateFormat().parse(json.getAsString()));
			}	
		}catch (ParseException e) {
			throw new JsonSyntaxException(json.getAsString(), e);
		}

		return calendar;
	}

	@Override
	public JsonElement serialize(Calendar calendar, Type typeOfSrc, JsonSerializationContext context) {
		String format = getDateTimeFormat().format(calendar.getTime());

		// verifica se possui hora minuto e segundo
		if (Pattern.compile(".*00.00.00$").matcher(format).matches()) {
			format = getDateFormat().format(calendar.getTime());
		}

		return new JsonPrimitive(format);
	}

	protected SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd");
	}

	protected SimpleDateFormat getDateTimeFormat() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
}