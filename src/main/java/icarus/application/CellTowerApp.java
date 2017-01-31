package icarus.application;

import java.io.IOException;
import java.util.Properties;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import icarus.generator.TowerScheduleGenerator;
import icarus.model.TowerSchedule;

/**
 * The Cell Tower application uses OptaPlanner to schedule cell phone towers onto a
 * location grid of latitudes and longitudes, attempting to provide coverage for the
 * best combination of high priority cell phones.
 * <p>
 * The solver configuration is defined by an optaplanner defined xml resource -
 * CellTowerSolverConfig.xml
 */
public class CellTowerApp
{
   /** The solver factory that generates an OptaPlanner solver for the problem space */
   private static SolverFactory<TowerSchedule> solverFactory;
   /** logger */
   private final static Logger logger = LoggerFactory.getLogger(CellTowerApp.class);
   /** Properties */
   private static Properties props;

   /**
    * Main entrance point for the application, this initializes Optaplanner's solver,
    * creates our cell phone and cell tower lists, and executes the solver, providing
    * metrics and solutions per pre-configured guidelines
    * <p>
    * Configuration is handled via an xml resource - CellTowerSolverConfig.xml
    *
    * @param args The type of scheduling run to perform
    */
   public static void main(String[] args)
   {
      solverFactory = SolverFactory.createFromXmlResource("cellTowerSolverConfig.xml");

      props = new Properties();
      try
      {
         props.load(ClassLoader.getSystemResourceAsStream("towerscheduler.properties"));
      }
      catch(IOException e)
      {
         logger.info("Failed to load properties file - " + e);
      }
      
      Solver<TowerSchedule> solver = solverFactory.buildSolver();
      TowerSchedule sched = TowerScheduleGenerator.createFromProperties(props);
      solver.solve(sched);
      logger.info(solver.getBestSolution().buildGeoJson());
   }
}
