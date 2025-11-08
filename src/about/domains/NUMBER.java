package about.domains;

import about.DomainAtom;

public class NUMBER implements DomainAtom {
    Number min;
    Number max;
    
    @Override
    public boolean isSupportable(Object value) {
        
        if(value==null) return false;
        if(value instanceof Number n){
            if (min == null && max == null) {
                return true;
            }
            double doubleValue = n.doubleValue();

            boolean withinMin = (min == null) || (doubleValue >= min.doubleValue());
            boolean withinMax = (max == null) || (doubleValue <= max.doubleValue());
            
            return withinMin && withinMax;
        } else {
            return false;
        }
    }

    public Number getMin() {
        return min;
    }

    public void setMin(Number min) {
        this.min = min;
    }

    public Number getMax() {
        return max;
    }

    public void setMax(Number max) {
        this.max = max;
    }

    @Override
    public String toString(){
        return "NUMBER(min:"+min+","+"max:"+max+")";
    }
}