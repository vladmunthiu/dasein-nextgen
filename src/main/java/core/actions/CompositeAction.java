package core.actions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vmunthiu on 2/16/2016.
 */
public class CompositeAction implements Action{
    private List<Action> actions;

    public CompositeAction(Action action) {
        this.actions = new ArrayList<Action>();
        this.actions.add(action);
    }

    @Override
    public void call() throws Exception {
        for(Action action : actions)
            action.call();
    }

    public CompositeAction then(Action action) {
        this.actions.add(action);
        return this;
    }
}
