
/**
 */
class Neuron
{
    private double[] weights; //weights
    private double[] temp; //temporary weights
    private double rateConstant; //rate constant
    private int row;
    private int col;
    private double lastOutput; //sigmoid function of the sum of the inputs*weights
    private double derv; //partial derivitive, the gradient with respect to weight
    private Network net;
    /**
     * creates a bare neuron with random weights
     */
    public Neuron(int numNeuron, int row, int col, Network net)
    {
        weights = new double[numNeuron+1];
        rateConstant = 1;
        this.row = row;
        this.col = col;
        this.net = net;
        for (int i = 0; i<weights.length; i++)
        {
            weights[i] = Math.random()*2-1;
        }
    }

    /**
     * Returns the value of sigmoid based on input and weight
     */
    public double feed(double[] inputs)
    {
        double[] copyIn = clone(inputs);
        if(copyIn.length - 1 >= 0)
        {
          copyIn[copyIn.length -1 ] = 1; //bias input
        }   
        double sum = 0;
        for (int i = 0; i < inputs.length; i++)
        {
            sum += copyIn[i]*weights[i]; //multiplies the inputs by weights
        }

        lastOutput = sigmoid(sum);
        return lastOutput;
    }

    /**
     * Adjusts weights based on errors
     */
    public void train(double[] inputs, double desired[])
    {
        temp = clone(weights);
        if (row == net.getNeurons().size() - 1) //if outer layer
        {
            if(desired.length>0)
                derv = (lastOutput - desired[col]) * lastOutput*(1-lastOutput);
        }
        else //if hidden layer
        {
            derv = 0;
            for (int c = 0; c < net.getNeurons().get(row+1).size(); c++)
            {
                Neuron n = net.getNeurons().get(row+1).get(c);
                derv += n.getPart() * n.getWeights()[col];
            }
            derv *= lastOutput * (1 - lastOutput);
        }
        for (int i = 0; i <weights.length; i++)
        {
            double in = 1;
            
            if (row == 0 && i != weights.length-1){
                if(inputs.length>0)
                  in = inputs[i];
            }
            else if(i != weights.length-1)
                in = net.getNeurons().get(row - 1).get(i).getLastOutput();
            else
                in = 1; //bias input
            temp[i] -= rateConstant * derv * in;
        }
    }
    
    public void updateWeights()
    {
        weights = temp;
    }
    
    /**
     * Logistic activation function
     */
    public static double sigmoid(double n)
    {
        return 1/(1+Math.exp(-n));
    }

    public double[] getWeights()
    {
        return weights;
    }

    public double getLastOutput()
    {
        return lastOutput;
    }

    public double getPart()
    {
        return derv;
    }
    
    public double[] clone(double[] in)
    {
        double[] copy = new double[in.length];
        for (int i = 0; i < in.length; i++)
        {
            copy[i]=in[i];
        }
        return copy;
    }
}
