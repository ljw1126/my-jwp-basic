package core.mvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
    private View view;
    private Map<String, Object> model = new HashMap<>();

    public ModelAndView(String viewName) {
        if(viewName == null) {
            this.view = new JsonView();
        } else {
            this.view = new JspView(viewName);
        }
    }

    public ModelAndView addAttribute(String key, Object value) {
        model.put(key, value);
        return this;
    }

    public Map<String, Object> getModel() {
        return Collections.unmodifiableMap(model);
    }

    public View getView() {
        return view;
    }
}
