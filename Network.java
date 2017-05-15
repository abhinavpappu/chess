import java.util.ArrayList;
/**
 * Network.java  
 *
 * @author:
 * Assignment #:
 * 
 * Brief Program Description:
 * 
 *
 */
class Network
{
    private ArrayList<ArrayList<Neuron>> neurons;
    private int inputs;
    /**
     * constructs the network of neurons as a nested arraylist
     */
    public Network(int[] size, int inputs)
    {
        neurons = new ArrayList<ArrayList<Neuron>>();
        this.inputs = inputs;
        for (int row = 0; row < size.length; row++)
        {
            ArrayList<Neuron> temp = new ArrayList<Neuron>();
            for (int col = 0; col < size[row]; col++)
            {
                if (row==0) //if outerlayer
                {
                    temp.add(new Neuron(inputs, row, col, this));
                }
                else //if inner layer
                {
                    temp.add(new Neuron(size[row-1], row, col, this));
                }
            }
            neurons.add(temp);
        }
    }
    /**
     * feeds forward every neuron, and determines the logistic based on weight
     */
    public double[] feed(double[] inputs)
    {   
        double[] output = new double[1];
        double[] temp;
        for (int row = 0; row < neurons.size(); row++)
        {
            if (row==0) //if outerlayer
            {
                output = new double[neurons.get(row).size()];
                for (int col = 0; col < neurons.get(row).size(); col++)
                {
                    output[col] = (neurons.get(row).get(col)).feed(inputs);
                }
            }
            else //if hiddenlayer
            {
                temp = new double[neurons.get(row).size()];
                for (int col = 0; col < neurons.get(row).size(); col++)
                {
                    temp[col] = (neurons.get(row).get(col)).feed(output);
                }
                output=temp;
            }
        }
        for(int  i = 0; i<output.length;i++)
            System.out.println(output[i]);
            return output;
    }
    public void train(double[] inputs, double[] desired)
    {
        feed(inputs);
        for (int row = neurons.size()-1; row >= 0; row--)
        {
            for (int col = neurons.get(row).size()-1; col >= 0; col--)
            {
                neurons.get(row).get(col).train(inputs, desired);
            }
        }
        
        for (ArrayList<Neuron> r: neurons)
        {
            for (Neuron n: r)
            {
                n.updateWeights();
            }
        }
    }
    public ArrayList<ArrayList<Neuron>> getNeurons()
    {
        return neurons;
    }
}
