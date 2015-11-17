package org.imaginationrobotics.team9826;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 11/15/2015.
 */
public class PIDController {
    private float proportionalValue;
    private float integralValue;
    private float derivativeValue;
    private int averagingSize;
    private List<Float> feedbackValues;
    private float error;
    private float prevError;
    private float derivativeError;
    private float integralError;
    private float maxOutput;
    private float minOutput;
    private float calculationValue;
    private float outputValue;

    public PIDController(){
        this.integralValue = 0;
        this.proportionalValue = 0;
        this.derivativeValue = 0;
        this.averagingSize = 0;
        this.maxOutput = 0;
        this.minOutput = 0;
        error = 0;
        integralError = 0;
        derivativeError = 0;
        prevError = 0;
        calculationValue = 0;
        outputValue = 0;
        feedbackValues = new ArrayList<Float>();
    }
    public PIDController(float proportionalValue, float integralValue, float derivativeValue, int averagingSize, float maxOutput, float minOutput){
        this.integralValue = integralValue;
        this.proportionalValue = proportionalValue;
        this.derivativeValue = derivativeValue;
        this.averagingSize = averagingSize;
        this.maxOutput = maxOutput;
        this.minOutput = minOutput;
        error = 0;
        integralError = 0;
        derivativeError = 0;
        prevError = 0;
        calculationValue = 0;
        outputValue = 0;
        feedbackValues = new ArrayList<Float>();
    }

    //***SETTERS AND GETTERS***
    public void setProportionalValue(float proportionalValue){
        this.proportionalValue = proportionalValue;
    }
    public float getProportionalValue(){
        return this.proportionalValue;
    }

    public void setIntegralValue(float integralValue){
        this.integralValue = integralValue;
    }
    public float getIntegralValue(){
        return this.integralValue;
    }

    public void setDerivativeValue(float derivativeValue){
        this.derivativeValue = derivativeValue;
    }
    public float getDerivativeValue(){
        return this.derivativeValue;
    }

    public void setAveragingSize(int averagingSize){
        this.averagingSize = averagingSize;
    }
    public int getAveragingSize(){
        return this.averagingSize;
    }

    public void setMaxOutput(float maxOutput){
        this.maxOutput = maxOutput;
    }
    public float getMaxOutput(){
        return this.maxOutput;
    }

    public void setMinOutput(float minOutput){
        this.minOutput = minOutput;
    }
    public float getMinOutput(){
        return this.minOutput;
    }

    public float getDerivativeError(){
        return this.derivativeError;
    }

    public float getPreviousError(){
        return this.prevError;
    }

    public float getIntegralError(){
        return this.integralError;
    }
    //***END SETTERS AND GETTERS***

    public float calculate(float setValue, float feedbackValue){
        //feedbackValue = feedbackAveraging(feedbackValue); Add in if feedback is inconsistent
        error = setValue - feedbackValue; //calculate error values

        derivativeError = (error - prevError);

        //PID algorithm
        calculationValue = ((proportionalValue*error) + (derivativeValue*derivativeError) + (integralValue*integralError));

        //prevent output value from exceeding max value
        if((outputValue + calculationValue) > maxOutput)
            outputValue = maxOutput;
        else if((outputValue + calculationValue) < minOutput)
            outputValue = minOutput;
        else //use calculation from PID equations to set ouput
            outputValue = outputValue + calculationValue;

        prevError = error;
        integralError = integralError + error;

        return outputValue;

    }

    float feedbackAveraging(float feedbackValue){
        float sum = 0;
        if(feedbackValues.size() > averagingSize)
            feedbackValues.remove(0);
        if(feedbackValue != 0)
            feedbackValues.add(feedbackValue);
        else {
            if(feedbackValues.size() > 0)
                feedbackValues.remove(0);
        }

        for(Float value: feedbackValues){
            sum += value;
        }

        if(feedbackValues.size() > 0)
            return sum / feedbackValues.size();
        else
            return 0;
    }

}

