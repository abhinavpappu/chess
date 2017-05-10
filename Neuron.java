
/**
 */
public class Neuron
{
    private double[] weights; //weights
    private double[] temp; //temporary weights
    private double rateConstant; //rate constant
    private int row;
    private int col;
    private double lastOutput; //sigmoid function of the sum of the inputs*weights
    private double part; //partial derivitive, the gradient with respect to weight
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
        double[] copyIn = remakeAs(inputs);
        copyIn[copyIn.length-1] = 1; //bias input
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
        temp = remakeAs(weights);
        if (row == net.getNeurons().size() - 1) //if outer layer
        {
            part = (lastOutput - desired[col]) * lastOutput*(1-lastOutput);
        }
        else //if hidden layer
        {
            part = 0;
            for (int c = 0; c < net.getNeurons().get(row+1).size(); c++)
            {
                Neuron n = net.getNeurons().get(row+1).get(c);
                part += n.getPart() * n.getWeights()[col];
            }
            part *= lastOutput * (1 - lastOutput);
        }
        for (int i = 0; i <weights.length; i++)
        {
            double in = 1;
            
            if (row == 0 && i != weights.length-1)
                in = inputs[i];
            else if(i != weights.length-1)
                in = net.getNeurons().get(row - 1).get(i).getLastOutput();
            else
                in = 1; //bias input
            temp[i] -= rateConstant * part * in;
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
        return part;
    }
    
    public double[] remakeAs(double[] in)
    {
        double[] copy = new double[in.length];
        for (int i = 0; i < in.length; i++)
        {
            copy[i]=in[i];
        }
        return copy;
    }
}
